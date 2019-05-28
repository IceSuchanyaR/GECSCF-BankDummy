package gec.scf.dummy.config.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger log = Logger.getLogger(AuthenticationSuccessEventListener.class);


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        log.info("Login success... " + e.getAuthentication());
    }

}
