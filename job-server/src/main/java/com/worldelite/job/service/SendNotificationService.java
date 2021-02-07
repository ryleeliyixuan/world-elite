package com.worldelite.job.service;

import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.constants.MessageType;
import com.worldelite.job.constants.RegistrationStatus;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.entity.*;
import com.worldelite.job.event.ActivityRegistrationEvent;
import com.worldelite.job.event.ActivityReviewEvent;
import com.worldelite.job.event.ActivityTakeOffEvent;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.mapper.*;
import com.worldelite.job.vo.ActivityReviewVo;
import com.worldelite.job.vo.UserApplicantVo;
import com.worldelite.job.vo.UserCorporateVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发送各种通知的服务
 *
 * @author Xiang Chao
 **/
@Slf4j
@Service
@AllArgsConstructor
public class SendNotificationService {

    private final FavoriteMapper favoriteMapper;
    private final RegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;
    private final ActivityTakeOffMapper activityTakeOffMapper;
    private final ActivityReviewService activityReviewService;
    private final MessageService messageService;
    private final MessageResource messageSource;
    private final UserApplicantService userApplicantService;
    private final UserCorporateService userCorporateService;
    private final IEmailService emailService;

    private final NotificationMessageMapper notificationMessageMapper;

    /**
     * 发送活动下架通知消息
     * <p>
     * 消息接受目标关注活动或报名参加活动的人
     */
    @Async
    @EventListener
    public void sendActivityTakeOffMsg(ActivityTakeOffEvent activityTakeOffEvent) {
        if (activityTakeOffEvent.getActivityId() == null) return;

        final Activity activity = activityMapper.selectByPrimaryKey(activityTakeOffEvent.getActivityId());
        if (activity == null) return;
        final ActivityTakeOff activityTakeOff = activityTakeOffMapper.selectByPrimaryKey(activityTakeOffEvent.getActivityId());
        if (activityTakeOff == null) return;

        //通知活动发布者
        String msg = messageSource.getMessage("activity.takeoff.notify", activity.getTitle(), activityTakeOff.getReason());
        sendMsg(activity.getUserId(), msg, String.format("/activity/%s", activity.getId()), 1);

        //通知活动关注者
        Favorite options = new Favorite();
        options.setType(FavoriteType.ACTIVITY.value);
        options.setObjectId(Long.valueOf(activityTakeOffEvent.getActivityId()));
        List<Favorite> favoriteList = favoriteMapper.selectAndList(options);

        String toFavoriteMsg = messageSource.getMessage("activity.favorite.takeoff.notify", activity.getTitle(), activityTakeOff.getReason());
        favoriteList.forEach(favorite -> {
            if (favorite.getObjectId() == null) return;
            sendMsg(favorite.getUserId(), toFavoriteMsg, String.format("/activity/%s", activity.getId()), 1);

        });

        //通知活动报名者
        RegistrationOptions registrationOptions = new RegistrationOptions();
        registrationOptions.setActivityId(activityTakeOffEvent.getActivityId());
        final List<Registration> registrations = registrationMapper.selectAndList(registrationOptions);

        String toRegistrationMsg = messageSource.getMessage("activity.registration.takeoff.notify", activity.getTitle(), activityTakeOff.getReason());
        registrations.forEach(registration -> {
            if (registration.getRegistrationUserId() == null) return;

            sendMsg(registration.getRegistrationUserId(), toRegistrationMsg, String.format("/activity/%s", activity.getId()), 1);
        });
    }

    /**
     * 发送活动报名审核通过/失败消息给报名者
     */
    @Async
    @EventListener
    public void sendActivityRegistrationReviewMsg(ActivityRegistrationEvent registrationEvent) {
        if (registrationEvent == null || registrationEvent.getRegistrationId() == null) return;

        final Registration registration = registrationMapper.selectByPrimaryKey(registrationEvent.getRegistrationId());
        if (registration == null) return;
        if (registration.getRegistrationUserId() == null) return;

        final Activity activity = activityMapper.selectByPrimaryKey(registration.getActivityId());
        if (activity == null) return;

        String toRegistrationMsg = null;

        NotificationMessage notificationMessage = notificationMessageMapper.selectByUserIdAndType(activity.getUserId().toString(),
                String.valueOf(MessageType.ACTIVITY_REGISTRATION_APPROVED.value), String.valueOf(activity.getId()));
        //更新消息通知表
        if (StringUtils.isNotBlank(registrationEvent.getNotifyMsg())) {
            //发送通知确认提示,0不再提示,1需要提示
            //需要提示的情况下每次更新最后发送的消息
            if ("1".equals(activity.getSendNoticeConfirm())) {
                if (notificationMessage == null) {
                    notificationMessage = new NotificationMessage();
                    notificationMessage.setUserId(activity.getUserId());
                    notificationMessage.setObjectId(String.valueOf(activity.getId()));
                    notificationMessage.setType(MessageType.ACTIVITY_REGISTRATION_APPROVED.value);
                    notificationMessage.setMsgContent(registrationEvent.getNotifyMsg());

                    notificationMessageMapper.insertSelective(notificationMessage);
                } else {
                    notificationMessage.setMsgContent(registrationEvent.getNotifyMsg());
                    notificationMessage.setUpdateTime(null);
                    notificationMessageMapper.updateByPrimaryKeySelective(notificationMessage);
                }

                toRegistrationMsg = registrationEvent.getNotifyMsg();
            } else {
                if (notificationMessage != null) {
                    toRegistrationMsg = notificationMessage.getMsgContent();
                }
            }
        } else {
            if (notificationMessage != null) {
                toRegistrationMsg = notificationMessage.getMsgContent();
            } else {
                if (registrationEvent.getStatus() == RegistrationStatus.PASS.value)
                    toRegistrationMsg = messageSource.getMessage("activity.registration.pass.notify", activity.getTitle());
                if (registrationEvent.getStatus() == RegistrationStatus.INAPPROPRIATE.value)
                    toRegistrationMsg = messageSource.getMessage("activity.registration.inappropriate.notify", activity.getTitle());
            }
        }

        sendMsg(registration.getRegistrationUserId(), toRegistrationMsg, String.format("/activity/%s", activity.getId()), 1);
    }

    /**
     * 发送活动审核通过/失败消息通知给活动发布者
     */
    @Async
    @EventListener
    public void sendActivityReviewMsg(ActivityReviewEvent reviewEvent) {
        if (reviewEvent == null || reviewEvent.getActivityId() == null) return;

        final Activity activity = activityMapper.selectByPrimaryKey(reviewEvent.getActivityId());
        if (activity == null) return;

        String toPublisherMsg = null;

        if (reviewEvent.getStatus() == VerificationStatus.PASS.value)
            toPublisherMsg = messageSource.getMessage("activity.review.pass.notify", activity.getTitle());
        if (reviewEvent.getStatus() == VerificationStatus.REJECT.value) {
            toPublisherMsg = messageSource.getMessage("activity.review.reject.notify", activity.getTitle(), reviewEvent.getReason());
        }

        //通知活动发布者
        sendMsg(activity.getUserId(), toPublisherMsg, String.format("/activity/%s", activity.getId()), 1);
    }

    private void sendMsg(Long userId, String msg, String url, int type) {
        Message message = new Message();
        message.setToUser(userId);
        message.setContent(msg);
        message.setUrl(url);
        message.setMsgType((byte) type);

        if (messageService.checkNotifyIsSent(message)) {
            log.warn("检测到活动通知消息重复发送,已取消. {}", message);
            return;
        }

        messageService.sendMessage(message);

        //邮件通知
        String email;
        UserApplicantVo applicantVo = userApplicantService.getUserInfo(userId);
        if (applicantVo != null) {
            email = applicantVo.getEmail();
        }else{
            final UserCorporateVo corporateVo = userCorporateService.getUserInfo(userId);
            email = corporateVo != null ? corporateVo.getEmail() : null;
        }

        if (StringUtils.isNotEmpty(email)) {
            EmailForm emailForm = new EmailForm();
            emailForm.setAddress(email);
            emailForm.setSubject(msg);
            emailForm.setEmailBody(msg);
            emailService.sendEmail(emailForm);
        }else{
            log.error("无法获取到用户的邮箱,邮件未发送");
        }
    }
}
