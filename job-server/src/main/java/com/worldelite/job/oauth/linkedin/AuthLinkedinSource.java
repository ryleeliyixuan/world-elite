package com.worldelite.job.oauth.linkedin;

import me.zhyd.oauth.config.AuthSource;

public enum AuthLinkedinSource implements AuthSource {
    /**
     * 领英
     */
    MYLINKEDIN {
        @Override
        public String authorize() {
            return "https://www.linkedin.com/oauth/v2/authorization";
        }

        @Override
        public String accessToken() {
            return "https://www.linkedin.com/oauth/v2/accessToken";
        }

        @Override
        public String userInfo() {
            return "https://api.linkedin.com/v2/me";
        }

        @Override
        public String refresh() {
            return "https://www.linkedin.com/oauth/v2/accessToken";
        }
    }
}
