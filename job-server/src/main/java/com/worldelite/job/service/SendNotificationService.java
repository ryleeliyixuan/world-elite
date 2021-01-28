package com.worldelite.job.service;

import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.constants.RegistrationStatus;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.entity.*;
import com.worldelite.job.event.ActivityRegistrationEvent;
import com.worldelite.job.event.ActivityTakeOffEvent;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.mapper.ActivityTakeOffMapper;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.mapper.RegistrationMapper;
import com.worldelite.job.vo.UserApplicantVo;
import lombok.AllArgsConstructor;
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
@Service
@AllArgsConstructor
public class SendNotificationService {

    private final FavoriteMapper favoriteMapper;
    private final RegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;
    private final ActivityTakeOffMapper activityTakeOffMapper;
    private final MessageService messageService;
    private final MessageResource messageSource;
    private final UserApplicantService userApplicantService;
    private final IEmailService emailService;

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
     * 发送活动报名审核通过/失败消息
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

        if (registrationEvent.getStatus() == RegistrationStatus.PASS.value)
            toRegistrationMsg = messageSource.getMessage("activity.registration.pass.notify", activity.getTitle());
        if (registrationEvent.getStatus() == RegistrationStatus.INAPPROPRIATE.value)
            toRegistrationMsg = messageSource.getMessage("activity.registration.inappropriate.notify", activity.getTitle());

        sendMsg(registration.getRegistrationUserId(), toRegistrationMsg, String.format("/activity/%s", activity.getId()), 1);
    }

    private void sendMsg(Long userId, String msg, String url, int type) {
        Message message = new Message();
        message.setToUser(userId);
        message.setContent(msg);
        message.setUrl(url);
        message.setMsgType((byte) type);
        messageService.sendMessage(message);

        //邮件通知
        UserApplicantVo user = userApplicantService.getUserInfo(userId);
        if (user != null) {
            String email = user.getEmail();
            if (StringUtils.isNotEmpty(email)) {
                EmailForm emailForm = new EmailForm();
                emailForm.setAddress(email);
                emailForm.setSubject(msg);
                emailForm.setEmailBody(msg);
                emailService.sendEmail(emailForm);
            }
        }
    }
}
