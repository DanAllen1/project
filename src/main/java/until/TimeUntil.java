package until;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class TimeUntil {

    //时间格式化，并转化为字符串，格式为年-月-日
    public String dateFormat(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    @Test
    public void time(){
        Timer timer = new Timer();
        TimerViewTask timerUntil = new TimerViewTask();
        timer.scheduleAtFixedRate(timerUntil,new Date(),2000);
        try
        {
            //多线程
            //timer线程休眠2000，让timerTask线程运行2000
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        //结束任务执行，程序终止
        //timer.cancel();
        //结束任务执行，程序并不终止,因为线程是JVM级别的
        //timer.cancel();
    }
}
