package am.user.json;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vanush all rights reserved.
 */

public class Space{

    private String article;
    private String video_link;
    private String rocket_name;
    private String launch_date_unix;
    private String mission_patch;
    private String details;


    public String getRocket_name() {
        return rocket_name;
    }

    public void setRocket_name(String rocket_name) {
        this.rocket_name = rocket_name;
    }

    public String getLaunch_date_unix() {
        return launch_date_unix;
    }

    public void setLaunch_date_unix(String launch_date_unix) {
        this.launch_date_unix = launch_date_unix;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getMission_patch() {
        return mission_patch;
    }

    public String getArticle() {
        return article;
    }

    public void setMission_patch(String mission_patch) {
        this.mission_patch = mission_patch;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Space(String rocket_name,
                 long launch_date_unix,
                 String mission_patch,
                 String details,
                 String article,
                 String video_link) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        this.launch_date_unix = convertTimeStampToDay(launch_date_unix);

        this.rocket_name = rocket_name;
        this.mission_patch = mission_patch;
        this.details = details;
        this.article = article;
        this.video_link = video_link;
    }

    private static String convertTimeStampToDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(timeStamp);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss");
        System.out.println(dateFormatter.format(calendar.getTime()));
        return dateFormatter.format(calendar.getTime());
    }


    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }
}