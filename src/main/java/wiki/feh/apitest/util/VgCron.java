package wiki.feh.apitest.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wiki.feh.apitest.facade.VgDataFacade;

@RequiredArgsConstructor
@Component
public class VgCron {
    private final VgDataFacade vgDataFacade;

    @Scheduled(cron = "0 5 * * * *") //* 5 * * * * 매 시 5분, 15분에 실행 (한시간에 한번만 실행할까?)
    public void CronVgData(){
        vgDataFacade.updateVgData();
    }

}
