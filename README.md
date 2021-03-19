# WorldElite: an online talent services platform
By Yixuan Rylee Li
2021-03-20 05:32:47 Saturday
## Introduction
World Elite is an online talent services platform that provides job seekers and recruiters with up-to-date recruitment information, aiming to facilitate the process of e-recruitment. The client-side operates three websites to serve job-seekers (ie. myworldelite.com), recruiters (ie. company.myworldelite.com), and administrators who supervise the operation of the previous two websites (ie. admin.myworldelite.com) respectively. The server-side runs five servers: job-server, instant messenger-server, mock-server, msg-server, and pay-server. 
- For Job-seekers: http://www.myworldelite.com/  
- For Recruiters: http://company.myworldelite.com/ 
- For Administrators/Staff: http://admin.myworldelite.com/dashboard 

## Deployment Guide
Direct to the corresponding folder, and run the following command to run the project. 
use` npm install` to install the dependencies in the local node_modules folder
use` npm run dev` to compile scripts

Note: The server-side is deployed remotely. APIs could be down as the server-side service went expired, causing a failed connection to the back-end. If so, please check out the demo(screenshots) folder and read through the demo section below.

## Responsibility and Commitment

[![Commitment](https://imgtu.com/i/6fWj29 "Commitment")](https://imgtu.com/i/6fWj29 "Commitment")
[![Commitment_2](https://imgtu.com/i/6fWX8J "Commitment_2")](https://imgtu.com/i/6fWX8J "Commitment_2")

**Daily Task:** From March 2020 to March 2021, I worked as a front-end engineer. My daily task is to build the front-end of the websites based on product manager’s prototype using HTML5, CSS, JavaScript. I used frontend frameworks (Vue.js), preprocessors (Sass and Less), and third-party tools (git, Redux, Ajax, Postman, Babel, Webpack). And I maintained code and fixed bugs on daily basis, enhancing the functionality and user experience based on feedback.

**Customized UI components:** I provided insight on a streamlined and user-friendly interface to product managers, translating UX wireframes and mockups into responsive, interactive features. I focused on modularity and reusability, built our own framework, and developed customized UI components with the UI/UX team, thereby creating a common collaborative ground between designers and developers. 

**Page Optimazation:** Realizing the impact of web performance optimization on user experience and business metrics, I took the initiative to establish a performance culture. After running performance experiments and measuring outcomes with A/B testing and DevTools, I built a proof of concept on how speed would benefit key performance indicators that we care about (e.g. daily active users, page view per user, page bounce rate, and conversation rate). Then, I worked closely with my colleagues to plan a set of realistic performance goals which combined Core Web Vitals (LCP, FID, and CLS) with custom-tailored metrics such as first/last visual change. I managed to improve these performance metrics by lazy-loading expensive components, using tree-shaking to reduce payload, and using service workers for caching and network fallback. As a result, the site loaded 32% faster; bounce rates decreased by 12%; our conversion rate was up 25%.

## Demo
Check out the Demo folder for screenshots of the websites.  
### Front-end
#### Job-web / myworldelite.com
Job-web / myworldelite.com incorporates six major sections:
1. Homepage: **(Developed by Me)**
- 	displays key events and promotions using carousel
- 	displays hottest jobs, hottest wiki, most recent jobs, and hottest workplace events requested from the back-end
2. Job:  **(Developed by Me)**
- 	provides a filter bar that features advanced search options, and list all the jobs that meet users’ search criteria
- 	features a Job-Detail page to present details of a certain job position; allows users to share, apply for, and subscribe to this job
3. Wiki: provides encyclopedias of over 100+ employers.  **(Developed by Me)**
- 	3.0 IndexPage: lists the top 4 EmployerWiki by industry
- 	3.1 IntroPage: uses chart and map components to present basic information of each employer (e.g. introduction, recruitment timeline, salary, employee data, company address, etc.)
- 	3.2 WikiJobPage: lists all the jobs associated with a certain company and provides a filter bar
- 	3.3 WikiActivityPage: lists all the activities associated with a certain company and provides a filter bar
- 	3.4 WikiCommunityPage: 
	- 3.4.1 WikiCommunityPostPage: provides discussion boards for job-seekers to share tips on the recruitment process of each company; displays a list of hottest posts on the right side.Users can make a new post. They can like, comment, report every post. And they can like, reply, report every comment. 
	- 3.4.2 WikiCommunityScorePage: provides rating boards for job-seekers to rate and comment on each company. Users can post a new rating with comments. They can like, comment, report every rating. And they can like, reply, report every comment below each rating. 
4. Activity **(Maintained by Me)**
- 	4.0 IndexPage: displays key events and promotions using carousel; provides a filter bar that features advanced search options, and list all the activities that meet users’ search criteria
- 	4.1 MyActivitiesPage: list all the activities that users follow / sign up for
- 	4.2 PublishPage: allows users to manage all recruiment activities posted by themselves
- 	4.3 EditActivityPage: allows users to post new recruiment activities, and provides templates of sign up sheet
- 	4.4 ActivityDetailPage: presents details of a certain recruitment event; allows users to share, apply for, and subscribe to this event
5. Mock: a peer-to-peer mock interview platform to match job seekers with current employees who could provide valuable tips
- 	5.1 MockHomePage: provides a filter bar that features advanced search options, and list all the interviewers that meet users’ search criteria
- 	5.2 MockDetailPage: presents details of a certain interviewer; allows users to make an appointment with him/her.
- 	5.3 MockPromisePage: displays all available slots of a certain interviewer, and allows users to choose a slot to make an appoinment.
- 	5.4 RegisterInteviewerPage: allows users to sign up as an interviewer
6. Other **(Developed by Me)**
- 	6.1 Message: displays all the notifications and messages users receivied
- 	6.2 Login and Sign in: features eight pages which allows users to log in, sign in, modify password, find password, fill up personal information (eg. avatar, nickname, job orientation), and get job recommendation upon finishing registration
- 6.3 Edit Resume: allows users to fill up their online resumes used to apply for jobs, and features the functionality of uploading attachments, previewing and downloading resumes.
- 6.4 MyFav: features four pages that displays all the companies/mock interviews/activities/jobs subscribed or applied by users

#### Job-company / company.myworldelite.com
Job-company / company.myworldelite.com consists of five major sections:
1. Job-Manage: manages all the positions posted, and posts new positions
2. Resume-Manage: manages all the resumes submitted by job-seekers to a certain position
3. Resume-Search: filters all the resumes that meet the users' search criteria
4. Activity-Manage: manages all the activities posted, and posts new activites with sign up sheet
5. Other
- 5.1 Message: displays all the notifications and messages users received
- 5.2 Login and Sign in: features eight pages which allows users to log in, sign in, modify password, find password, edit company information (eg. avatar, nickname, introduction)

#### Job-admin / admin.myworldelite.com (Developed By Me)
Job-admin / admin.myworldelite.com consists of four major sections:
1. Dashboard: displays website traffic and user stat 
2. Wiki-Manage: 
- manages all the EmployeeWikis, and post new EmployeeWiki
- manages all the comments and replies posted on the CommunityScore and CommunityPost section; admins can view and delete posts as well as set them as recommended. 
3. User-Manage: manages all the users by type (job-seekers/recruiters/admins)
4. Other: manages companies / resumes / job positions / events / mock interviews, and features other configuration setting.
