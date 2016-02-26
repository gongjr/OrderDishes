package com.asiainfo.orderdishes;

public class Constants {

    /**
     * 服务器版本文件update.xml地址
     */
    public static final String VersionServerUrl = "http://115.29.35.199:17890/android-apk/OrderDishes-update.xml";
    /**
     *
     * Beautiful测试环境
     */
    public static final String Beautiful_tst = "http://115.29.35.199:58080/Beautiful";
    /**
     * Beautiful生产环境
     */
    public static final String Beautiful = "http://115.29.35.199:38080/Beautiful";
    /**
     * buildsite测试环境
     */
    public static final String BuildSite = "http://115.29.35.199:19890/buildsite";
    /**
     * 塔克测试环境
     */
    public static final String tacos_tst = "http://115.29.35.199:19890/tacos";
    /**
     * 塔克生产环境
     */
    public static final String tacos = "http://115.29.35.199:27890/tacos";
    /**
     * 塔克生产验证环境
     */
    public static final String tacos_tst2 = "http://115.29.35.199:29890/tacos";
    /**
     * 塔克测试环境
     */
    public static final String tacos_tst1 = "http://115.29.35.199:18888/tacos";
    /**
     * buildsite生产环境
     */
//	public static final String BuildSite = "http://115.29.35.199:29890/buildsite";
//	public static final String BuildSite = "http://www.kxlive.com/buildsite";
    /**
     * 网络地址前缀
     */
    public static String address = tacos;

    /**
     * DisheesSelectionActivity到DishesHomeActivity的返回码，返回点菜页，取消操作
     */
    public static final int DishesSelection_resultcode_return = 1;
    /**
     * DisheesSelectionActivity到DishesHomeActivity的返回码，结束点菜页，查看订单详情
     */
    public static final int DishesSelection_resultcode_check = 2;
    /**
     * DisheesSelectionActivity到DishesHomeActivity的返回码，结束点菜页,返回大厅
     */
    public static final int DishesSelection_resultcode_back = 3;

    /**
     * DishesHomeActivity请求道DisheesSelectionActivity的请求码
     */
    public static final int DishesHome_requestcode_dishesselect = 1;
    /**
     * DishesHomeActivity请求道DisheesSetActivity的请求码
     */
    public static final int DishesHome_requestcode_dishesset = 2;

    // 网络访问errorcode
    public static final String ERRORCODE_SUCCESS = "0";
    public static final String ERRORCODE_FAIL = "1";
    // 点菜订单的操作状态，1开桌，2加菜，3编辑
    public static final int DishesHome_orderOperateState_open = 1;
    public static final int DishesHome_orderOperateState_add = 2;
    public static final int DishesHome_orderOperateState_edit = 3;

    // 大厅中对桌子订单的操作类型 换桌
    public static final int HALL_ACTION_CHANGE_DESK = 0;
    public static final int HALL_ACTION_VIEW_ORDER = 1;
    public static final int HALL_ACTION_MAKE_ORDER = 2;

    // 查看订单返回码
    public static final int ElectronicPayment_resultcode_back = 1;
    public static final int ElectronicPayment_resultcode_finish = 2;
    public static final int OrderDetail_requestcode_ElectronicPayment = 1;

    //加菜请求返回码
    public static final int DishesHome_resultcode_back = 1;
    public static final int DiskOrder_requestcode_add = 2;
    public static final int DiskOrder_requestcode_edit = 3;

    // Handler主线程延时消息统一调用标识符
    public static final int Handler_Dialog_Delay = 1;
    public static final int Handler_DishesHome_initDelay = 2;

    public static final int QueryUnfinishedOrder_type_lookorder = 1;
    public static final int QueryUnfinishedOrder_type_opentable = 2;

    // 通知后厨标识
    public static final String notityKitchen_now = "1";
    public static final String notityKitchen_wait = "0";

    // 是否更改桌子状态
    public static final String changeDesk_yes = "1";
    public static final String changeDesk_no = "0";

    public static final int Dishesset_Resultcode_Sucesse = 1;
    public static final int Dishesset_Resultcode_Cancle = 2;

    public static final String EXIT_ACTION="FLAG_GlobalVarable_EXIT_ACTION";

}
