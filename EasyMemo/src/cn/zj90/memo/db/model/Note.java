package cn.zj90.memo.db.model;

/**
 * Created by zj90
 * Time : 2015/3/18 14:41.
 */
public class Note {
    private int id;
    private String time;
    private int priority;
    private String content;
    private String shortContent;

    public static final class Priority {
        public static final int NORMAL = 0;
        public static final int IMPORTANT = 1;
        public static final int URGENT = 2;
    }


    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public int getPriority() {
        return priority;
    }

    public String getContent() {
        return content;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }
}
