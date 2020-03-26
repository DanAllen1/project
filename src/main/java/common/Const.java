package common;

public class Const {

    public  interface CustomerMark{
        int NO_MARK = 0;
        int MARK= 1;
    }
    public interface UserRole{
        public static final int ROLE_NONE = 0; //未经批准的账号
        public static final int ROLE_NORMAL = 1; //普通用户
        public static final int ROLE_RETAILER = 2; //商家
        public static final int ROLE_ADMIN = 3; //管理员
    }

    public interface ProjectSort{
        int DEFAULT =0;
        int DATE_ASC = 1;
        int DATE_DESC = 2;
    }
}
