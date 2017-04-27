//package com.test.lxh.utils;
//
//import com.test.lxh.utils.Pos;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by haoguibao on 16/4/28.
// * Description : 我的打印实现类
// * Revision :
// */
//public class MyPrintTask implements Runnable {
//    private final String jsonmsg;
//    //打印机列表
//    private final List<PrintMachineBean> listPrintMachine;
//    private SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
//    private SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
//    private FoodsDetail foodsDetail;
//    //点餐
//    private List<FoodsDetail> listfoodsDetail = new ArrayList<FoodsDetail>();
//    //加菜
//    private List<FoodsDetail> listfoodsDetailAdd = new ArrayList<FoodsDetail>();
//    //退菜
//    private List<FoodsDetail> listfoodsDetailRetreat = new ArrayList<FoodsDetail>();
//    //点餐
//    private List<FoodsDetail> listfoodsDetailTicket = new ArrayList<FoodsDetail>();
//    //加菜
//    private List<FoodsDetail> listfoodsDetailAddTicket = new ArrayList<FoodsDetail>();
//    //退菜
//    private List<FoodsDetail> listfoodsDetailRetreatTicket = new ArrayList<FoodsDetail>();
//    //催菜
//    private List<FoodsDetail> listfoodsDetailPress = new ArrayList<FoodsDetail>();
//    private String shopName = SharedTools.getShopName(BaseApplication.getContext());
//    private int order_id;
//    private String table_no;
//    private String pay_by;
//    private String waiterName;
//    private String orderRemark;
//    private int cost;
//    private int amount;
//    //数字格式
//    private DecimalFormat decimalFormat = new DecimalFormat("0");
//    private DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
//    private int pay_state;
//    private int confirmed;   // 为1代表后厨打印
//    private int ticket;         //  为1代表前台打印
//    private String time;
//    private String date_time;
//    private String type;
//    //仍有订单未打印，不要打印催菜单
//    private boolean unPrint = false;
//    private String before_table;
//    private String day_sn;
//    private boolean isDaySn;      //订单序号
//
//    public MyPrintTask(String jsonmsg) {
//        this.jsonmsg = jsonmsg;
//        listPrintMachine = CommonConstant.listPrintMachine;
//    }
//
//    @Override
//    public void run() {
//        initData();
//        //打印前台票
//        if (ticket == 1) {
//            printTicket();
//        }
//        //打印后厨票
//        if (confirmed == 1) {
//            printKitchen();
//        }
//    }
//
//    /**
//     * 解析打印单数据
//     */
//    private void initData() {
//        try {
//            JSONObject jsonObject = new JSONObject(jsonmsg);
//            //判断是服务员订单  waiter ，还是用户订单  customer
//            type = jsonObject.optString("type");
//            confirmed = jsonObject.optInt("confirmed");//打印后厨票时，confirmed为1，不打印为0
//            ticket = jsonObject.optInt("ticket");      //打印前台票时，ticket 为 1，不打印为0
//            table_no = jsonObject.optString("table_no");//桌号
//            Date created = new Date(jsonObject.optInt("created") * 1000L);
//            order_id = jsonObject.optInt("id");           //订单号
//            //备注：remark
//            orderRemark = jsonObject.optString("remark");
//            //是否有换桌
//            before_table = jsonObject.optString("before_table");
//            //每日的订单序号：支付完以后才不为null，并自动 +1
//            day_sn = jsonObject.optString("day_sn");
//            //如果订单序号不为空，则打印订单序号
//            if (!day_sn.isEmpty() && !"null".equals(day_sn)) {
//                isDaySn = true;
//            }
//            if (ticket == 1) {
//                //支付状态
//                pay_state = jsonObject.optInt("pay_state");
//                date_time = format.format(created);                 //订单日期+时间
//                //用户名,字段太乱，以后再加
//                //                        JSONObject user = jsonObject.getJSONObject("user");
//                //                        String nickname = user.optString("nickname");       //通常是用户的微信昵称
//                //                        String truename = user.optString("truename");
//                //                        String username = user.optString("username");
//                //桌号  table_no
//                //订单时间   time
//                //付款方式  （可以为空）  （现金支付（服务员：赵婷））
//                pay_by = jsonObject.optString("pay_by");
//                //服务员：waiterName
//                waiterName = SharedTools.getWaiterName(BaseApplication.getContext());
//                //订单内容   text[0] = "菜品";text[1] = "单价";text[2] = "数量";text[3] = "小计";
//                JSONArray details = jsonObject.getJSONArray("details");
//                //String category_id, String food_name, String price, String qty, String subtotal
//                for (int i = 0; i < details.length(); i++) {
//                    //菜品属性bean
//                    JSONObject food_detail = (JSONObject) details.get(i);
//                    //                    String cate_id = food_detail.optString("cate_id");      //种类id
//                    String food = food_detail.optString("food");            //食物名字
//                    String price = food_detail.optString("price");          //单价
//                    double p1 = Double.parseDouble(price);
//                    String qty = food_detail.optString("qty");              //菜品数量（2.00）
//                    double q1 = Double.parseDouble(qty);
//                    //                    String subtotal = food_detail.optString("subtotal");    //菜品总价（单价*数量）
//                    String retreat = food_detail.optString("retreat");      //退菜的数量
//                    double r1 = Double.parseDouble(retreat);
//                    String retreatMoney = decimalFormat2.format(r1 * p1);       //退菜退的钱
//                    String add = food_detail.optString("add");              //加菜的次数
//                    //显示数量showqty  为目前剩余的+退菜的
//                    double showqty = q1 + r1;
//                    //展示的小计    showqty*price
//                    String subtotal = decimalFormat2.format(showqty * p1);
//                    foodsDetail = new FoodsDetail(food, price, decimalFormat.format(showqty), subtotal, decimalFormat.format(r1), retreatMoney);
//                    if ("0".equals(add)) {
//                        //只有点菜 。（没有退菜,没有加菜）
//                        listfoodsDetailTicket.add(foodsDetail);
//                    } else {
//                        //加菜
//                        listfoodsDetailAddTicket.add(foodsDetail);
//                    }
//                    if (!"0.00".equals(retreat)) {
//                        //只有退菜
//                        listfoodsDetailRetreatTicket.add(foodsDetail);
//                    }
//                }
//                //合计： 总钱，没有折扣，减免，退卡之前的
//                cost = jsonObject.optInt("cost");
//                //折扣
//                //减免
//                //实际支付：
//                amount = jsonObject.optInt("amount");
//                //需退款金额：
//                //华丽丽的分界线  ----------------------------------------------
//                //欢迎下次光临
//            }
//            if (confirmed == 1) {
//                time = format2.format(created);                 //订单时间(不包含日期)
//                JSONArray details = jsonObject.getJSONArray("details");
//                for (int i = 0; i < details.length(); i++) {
//                    //菜品属性bean
//                    JSONObject food_detail = (JSONObject) details.get(i);
//                    String cate_id = food_detail.optString("cate_id");      //种类id
//                    String food = food_detail.optString("food");            //食物名字
//                    String id = food_detail.optString("id");
//                    String qty = food_detail.optString("qty");              //菜品数量（2.00）
//                    double q1 = Double.parseDouble(qty);
//                    String retreat = food_detail.optString("retreat");      //退菜的数量
//                    double r1 = Double.parseDouble(retreat);
//                    String add = food_detail.optString("add");              //加菜的次数
//                    //对该菜的备注
//                    String remark = food_detail.optString("remark");
//                    if ("null".equals(remark)) {
//                        remark = "";
//                    }
//                    String printed = food_detail.optString("printed");          //菜品是否已经被打印过了
//                    String retreat_printed = food_detail.optString("retreat_printed");      //已经退菜的数量
//                    String updated = food_detail.optString("updated");          //每个菜品的下单时间。
//                    if ("null".equals(updated) || "".equals(updated)) {
//                        updated = "";
//                    } else {
//                        Date dtUpdated = new Date(Integer.parseInt(updated) * 1000L);
//                        updated = format2.format(dtUpdated);                 //订单时间(不包含日期)
//                    }
//                    foodsDetail = new FoodsDetail(id, cate_id, food, decimalFormat.format(q1), decimalFormat.format(r1), remark, add, printed, retreat_printed, updated);
//                    if ("0".equals(foodsDetail.getPrinted())) {
//                        unPrint = true;
//                        if ("0.00".equals(retreat)) {
//                            if ("0".equals(add)) {
//                                //只有点菜 。（没有退菜,没有加菜）
//                                listfoodsDetail.add(foodsDetail);
//                            } else if (!"0".equals(add)) {
//                                //加菜
//                                listfoodsDetailAdd.add(foodsDetail);
//                            }
//                        } else {
//                            //退菜标记为0
//                            //只有退菜
//                            listfoodsDetailRetreat.add(foodsDetail);
//                        }
//                    } else {
//                        if (!"0".equals(foodsDetail.getQty())) {
//                            //添加到催菜单
//                            listfoodsDetailPress.add(foodsDetail);
//                        }
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 后厨打印
//     * 点餐类型 （点菜单，催菜单，加菜单，退菜单）
//     */
//    private void printKitchen() {
//        //点餐
//        printOrdering();
//        //加菜
//        printAddOrder();
//        //退菜
//        printRetreatOrder();
//        //催菜单
//        if (!unPrint) {
//            printPressOrder();
//        }
//    }
//
//    /**
//     * 点餐
//     */
//    private void printOrdering() {
//        //点餐
//        if (listfoodsDetail != null && listfoodsDetail.size() > 0) {
//            Pos pos = null;
//            //创建strId 用了存储打印过的菜品id
//            String strId = "";
//            for (PrintMachineBean printMachineBean : listPrintMachine) {
//                String category_number = "," + printMachineBean.getCategory_number() + ",";
//                String ip = printMachineBean.getIp();         //打印机ip
//                String kit_type = printMachineBean.getKit_type();       //是否 一菜一单  1一菜一单；0整单打印',
//                //是否打印桌台等内容的标识
//                boolean isFirstPrint = true;
//                //是否有打印内容
//                boolean isHavePrint = false;
//                //打印纸是80
//                try {
//                    if (ping(ip)) {
//                        sleep(800);         //睡 1 秒
//                        try {
//                            pos = new Pos(ip, 9100, "GBK");    //第一个参数是打印机网口IP
//                            for (FoodsDetail foodsDetail : listfoodsDetail) {
//                                if (category_number.contains("," + foodsDetail.getCategory_id() + ",")) {
//                                    if ("0".equals(kit_type)) {
//                                        if (isFirstPrint) {
//                                            //初始化打印机
//                                            pos.initPos();
//                                            //                                pos.bold(true);
//                                            pos.fontSize(2);
//                                            pos.printTextNewLine("点菜单");
//                                            //                                        pos.printTextNewLine("--------------------");       //80的
//                                            pos.printTextNewLine("----------------");        //58的
//                                            //如果订单序号不为空，则打印订单序号
//                                            if (isDaySn) {
//                                                pos.printTextNewLine("订单序号：");
//                                                pos.printText(day_sn);
//                                            }
//                                            pos.printTextNewLine("桌号：");
//                                            pos.printText(table_no);
//                                            //如果有换桌操作，则显示原桌号
//                                            if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                                pos.printTextNewLine("原桌号：");
//                                                pos.printText(before_table);
//                                            }
//                                            pos.printTextNewLine("时间：");
//                                            pos.printText(time);
//                                            pos.printTextNewLine("备注：");
//                                            if (!orderRemark.isEmpty() && !"null".equals(orderRemark)) {
//                                                pos.printText(orderRemark);
//                                            } else {
//                                                pos.printText("");
//                                            }
//                                            pos.printTextNewLine("----------------");
//                                            //不再是第一次打印，接着上面的东西不用打印了
//                                            isFirstPrint = false;
//                                        }
//                                        pos.printTextNewLine(foodsDetail.getFood_name());
//                                        pos.printLocation(20, 1);
//                                        pos.printText("x");
//                                        pos.printLocation(70, 1);
//                                        pos.printText(foodsDetail.getQty());
//                                        pos.printWordSpace(1);
//                                        pos.printText(foodsDetail.getRemark());
//                                        //已打印，需要切纸
//                                        isHavePrint = true;
//                                    } else {
//                                        //初始化打印机
//                                        pos.initPos();
//                                        //                                pos.bold(true);
//                                        pos.fontSize(2);
//                                        pos.printTextNewLine("点菜单");
//                                        //                                        pos.printTextNewLine("--------------------");       //80的
//                                        pos.printTextNewLine("----------------");        //58的
//                                        //如果订单序号不为空，则打印订单序号
//                                        if (isDaySn) {
//                                            pos.printTextNewLine("订单序号：");
//                                            pos.printText(day_sn);
//                                        }
//                                        pos.printTextNewLine("桌号：");
//                                        pos.printText(table_no);
//                                        //如果有换桌操作，则显示原桌号
//                                        if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                            pos.printTextNewLine("原桌号：");
//                                            pos.printText(before_table);
//                                        }
//                                        pos.printTextNewLine("时间：");
//                                        pos.printText(time);
//                                        pos.printTextNewLine("备注：");
//                                        if (!orderRemark.isEmpty() && !"null".equals(orderRemark)) {
//                                            pos.printText(orderRemark);
//                                        } else {
//                                            pos.printText("");
//                                        }
//                                        pos.printTextNewLine("----------------");
//                                        //不再是第一次打印，接着上面的东西不用打印了
//                                        pos.printTextNewLine(foodsDetail.getFood_name());
//                                        pos.printLocation(20, 1);
//                                        pos.printText("x");
//                                        pos.printLocation(70, 1);
//                                        pos.printText(foodsDetail.getQty());
//                                        pos.printWordSpace(1);
//                                        pos.printText(foodsDetail.getRemark());
//                                        //防止内容没有打印是包异常
//                                        pos.printLine(3);
//                                        //切纸
//                                        pos.feedAndCut();
//                                    }
//                                    strId = strId + foodsDetail.getFood_id() + ",";
//                                }
//                            }
//                            if (isHavePrint) {
//                                //防止内容没有打印是包异常
//                                pos.printLine(3);
//                                //切纸
//                                pos.feedAndCut();
//                            }
//                            if (pos != null) {
//                                //关闭打印IO流
//                                pos.closeIOAndSocket();
//                            }
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "UnknownHostException");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "IOException");
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            //把已经打印的菜品，做一下标记（需要访问服务器，标记完以后 printed 字段的值变为了1）token, ids, type, order_id, retreat = null
//            postPrintedFoodId(strId, "");
//        }
//    }
//
//    /**
//     * 加菜
//     */
//    private void printAddOrder() {
//        if (listfoodsDetailAdd != null && listfoodsDetailAdd.size() > 0) {
//            Pos pos = null;
//            //创建strId 用了存储打印过的菜品id
//            String strId = "";
//            for (PrintMachineBean printMachineBean : listPrintMachine) {
//                String category_number = "," + printMachineBean.getCategory_number() + ",";
//                String ip = printMachineBean.getIp();         //打印机ip
//                String kit_type = printMachineBean.getKit_type();
//                //是否打印桌台等内容的标识
//                boolean isFirstPrint = true;
//                //是否有打印内容
//                boolean isHavePrint = false;
//                try {
//                    if (ping(ip)) {
//                        sleep(800);         //睡 1 秒
//                        try {
//                            pos = new Pos(ip, 9100, "GBK");    //第一个参数是打印机网口IP
//                            for (FoodsDetail foodsDetail : listfoodsDetailAdd) {
//                                if (category_number.contains("," + foodsDetail.getCategory_id() + ",")) {
//                                    if ("0".equals(kit_type)) {
//                                        if (isFirstPrint) {
//                                            //初始化打印机
//                                            pos.initPos();
//                                            //                                pos.bold(true);
//                                            pos.fontSize(2);
//                                            pos.printTextNewLine("加菜单");
//                                            pos.printTextNewLine("----------------");
//                                            pos.printTextNewLine("桌号：");
//                                            pos.printText(table_no);
//                                            pos.printTextNewLine("时间：");
//                                            //如果加菜的时间不为空"",则显示更新时间
//                                            String updated = foodsDetail.getUpdated();
//                                            if (!"".equals(updated)) {
//                                                pos.printText(updated);
//                                            } else {
//                                                pos.printText(time);
//                                            }
//                                            //如果有换桌操作，则显示原桌号
//                                            if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                                pos.printTextNewLine("原桌号：");
//                                                pos.printText(before_table);
//                                            }
//                                            pos.printTextNewLine("----------------");
//                                            //不再是第一次打印，接着上面的东西不用打印了
//                                            isFirstPrint = false;
//                                        }
//                                        pos.printTextNewLine(foodsDetail.getFood_name());
//                                        pos.printLocation(20, 1);
//                                        pos.printText("x");
//                                        pos.printLocation(70, 1);
//                                        pos.printText(foodsDetail.getQty());
//                                        pos.printWordSpace(1);
//                                        pos.printText(foodsDetail.getRemark());
//                                        isHavePrint = true;
//                                    } else {
//                                        //初始化打印机
//                                        pos.initPos();
//                                        //                                pos.bold(true);
//                                        pos.fontSize(2);
//                                        pos.printTextNewLine("加菜单");
//                                        pos.printTextNewLine("----------------");
//                                        pos.printTextNewLine("桌号：");
//                                        pos.printText(table_no);
//                                        pos.printTextNewLine("时间：");
//                                        //如果加菜的时间不为空"",则显示更新时间
//                                        String updated = foodsDetail.getUpdated();
//                                        if (!"".equals(updated)) {
//                                            pos.printText(updated);
//                                        } else {
//                                            pos.printText(time);
//                                        }
//                                        //如果有换桌操作，则显示原桌号
//                                        if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                            pos.printTextNewLine("原桌号：");
//                                            pos.printText(before_table);
//                                        }
//                                        pos.printTextNewLine("----------------");
//                                        pos.printTextNewLine(foodsDetail.getFood_name());
//                                        pos.printLocation(20, 1);
//                                        pos.printText("x");
//                                        pos.printLocation(70, 1);
//                                        pos.printText(foodsDetail.getQty());
//                                        pos.printWordSpace(1);
//                                        pos.printText(foodsDetail.getRemark());
//                                        //防止内容没有打印是包异常
//                                        pos.printLine(3);
//                                        //切纸
//                                        pos.feedAndCut();
//                                    }
//                                    strId = strId + foodsDetail.getFood_id() + ",";
//                                }
//                            }
//                            //防止没有打印内容时报异常
//                            if (isHavePrint) {
//                                pos.printLine(3);
//                                //切纸
//                                pos.feedAndCut();
//                            }
//                            if (pos != null) {
//                                //关闭打印IO流
//                                pos.closeIOAndSocket();
//                            }
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "UnknownHostException");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "IOException");
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            //把已经打印的菜品，做一下标记（需要访问服务器，标记完以后 printed 字段的值变为了1）token, ids, type, order_id, retreat = null
//            postPrintedFoodId(strId, "");
//        }
//    }
//
//    /**
//     * 退菜
//     */
//    private void printRetreatOrder() {
//        if (listfoodsDetailRetreat != null && listfoodsDetailRetreat.size() > 0) {
//            Pos pos = null;
//            //创建strId 用了存储打印过的菜品id
//            String strId = "";
//            //创建strRetreat ,用于保存对应菜品id的退菜数量
//            String strRetreat = "";
//            for (PrintMachineBean printMachineBean : listPrintMachine) {
//                String category_number = "," + printMachineBean.getCategory_number() + ",";
//                String ip = printMachineBean.getIp();         //打印机ip
//                String kit_type = printMachineBean.getKit_type();
//                //是否打印桌台等内容的标识
//                boolean isFirstPrint = true;
//                //是否有打印内容
//                boolean isHavePrint = false;
//                try {
//                    if (ping(ip)) {
//                        sleep(800);         //睡 1 秒
//                        try {
//                            pos = new Pos(ip, 9100, "GBK");    //第一个参数是打印机网口IP
//                            for (FoodsDetail foodsDetail : listfoodsDetailRetreat) {
//                                if (category_number.contains("," + foodsDetail.getCategory_id() + ",")) {
//                                    //有关退菜的一些数据
//                                    String retreat_printed = foodsDetail.getRetreat_printed();//已经打印的该菜的退菜数量
//                                    String retreat = foodsDetail.getRetreat();//总共的退菜的数量
//                                    double dbRetreat = Double.parseDouble(retreat);
//                                    double dbRetreatPrinted = Double.parseDouble(retreat_printed);
//                                    String showRetreat = decimalFormat.format(dbRetreat - dbRetreatPrinted);
//                                    //----------------
//                                    if ("0".equals(kit_type)) {
//                                        if (isFirstPrint) {
//                                            //初始化打印机
//                                            pos.initPos();
//                                            pos.fontSize(3);        //放大字体
//                                            pos.printTextNewLine("退菜单");
//                                            pos.fontSize(2);        //稍微小点的字体
//                                            pos.printTextNewLine("----------------");
//                                            pos.printTextNewLine("桌号：");
//                                            pos.printText(table_no);
//                                            pos.printTextNewLine("时间：");
//                                            //如果退菜的时间不为空"",则显示更新时间
//                                            String updated = foodsDetail.getUpdated();
//                                            if (!"".equals(updated)) {
//                                                pos.printText(updated);
//                                            } else {
//                                                pos.printText(time);
//                                            }
//                                            //如果有换桌操作，则显示原桌号
//                                            if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                                pos.printTextNewLine("原桌号：");
//                                                pos.printText(before_table);
//                                            }
//                                            pos.printTextNewLine("----------------");
//                                            isFirstPrint = false;
//                                        }
//                                        pos.printTextNewLine("[退]");
//                                        pos.printText(foodsDetail.getFood_name());
//                                        pos.printLocation(20, 1);
//                                        pos.printText("x");
//                                        pos.printLocation(70, 1);
//                                        pos.printText(showRetreat);
//                                        isHavePrint = true;       //有打印内容
//                                    } else {
//                                        //初始化打印机
//                                        pos.initPos();
//                                        pos.fontSize(3);        //放大字体
//                                        pos.printTextNewLine("退菜单");
//                                        pos.fontSize(2);        //稍微小点的字体
//                                        pos.printTextNewLine("----------------");
//                                        pos.printTextNewLine("桌号：");
//                                        pos.printText(table_no);
//                                        pos.printTextNewLine("时间：");
//                                        //如果退菜的时间不为空"",则显示更新时间
//                                        String updated = foodsDetail.getUpdated();
//                                        if (!"".equals(updated)) {
//                                            pos.printText(updated);
//                                        } else {
//                                            pos.printText(time);
//                                        }
//                                        //如果有换桌操作，则显示原桌号
//                                        if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                            pos.printTextNewLine("原桌号：");
//                                            pos.printText(before_table);
//                                        }
//                                        pos.printTextNewLine("----------------");
//                                        pos.printTextNewLine("[退]");
//                                        pos.printText(foodsDetail.getFood_name());
//                                        pos.printLocation(20, 1);
//                                        pos.printText("x");
//                                        pos.printLocation(70, 1);
//                                        pos.printText(showRetreat);
//                                        //防止内容没有打印是包异常
//                                        pos.printLine(3);
//                                        //切纸
//                                        pos.feedAndCut();
//                                    }
//                                    strId = strId + foodsDetail.getFood_id() + ",";
//                                    strRetreat = strRetreat + showRetreat + ",";
//                                }
//                            }
//                            if (isHavePrint) {
//                                //防止内容没有打印是包异常
//                                pos.printLine(3);
//                                //切纸
//                                pos.feedAndCut();
//                            }
//                            if (pos != null) {
//                                //关闭打印IO流
//                                pos.closeIOAndSocket();
//                            }
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "UnknownHostException");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "IOException");
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            //把已经打印的退菜的菜品，做一下标记（需要访问服务器，标记完以后 printed 字段的值变为了1）token, ids, type, order_id, retreat = null
//            //retreat参数为退菜的打印数量，例如总量为5个，第一次退了1个，第二次退了3个，则分别携带1，3；
//            //不同菜品使用逗号分隔，顺序必须保证和ids的顺序一致，若存在无需退的则保持为空。
//            //例如ids为1,5,7的三个菜品，1退了3个，5、7没有则retreat参数为“3,,”。如果ids中没有退菜的则retreat不携带。
//            //打印订单socket返回的参数中retreat_printed为已打印过的退菜数量，例如总量为5个的菜品retreat_printed参数为3，retreat为4，则本次打印1，打印过后携带1请求本接口。
//            postPrintedFoodId(strId, strRetreat);
//        }
//    }
//
//    /**
//     * 催菜单
//     */
//    private void printPressOrder() {
//        if (listfoodsDetailPress != null && listfoodsDetailPress.size() > 0) {
//            Pos pos = null;
//            for (PrintMachineBean printMachineBean : listPrintMachine) {
//                String category_number = "," + printMachineBean.getCategory_number() + ",";     //该打印机可以打印的种类
//                String ip = printMachineBean.getIp();         //打印机ip
//                String kit_type = printMachineBean.getKit_type();
//                //是否打印桌台等内容的标识
//                boolean isFirstPrint = true;
//                //是否有打印内容
//                boolean isHavePrint = false;
//                //是否是转台单
//                boolean noChangeTable = true;
//                try {
//                    //启动打印程序之前先ping 一下打印机，如果能ping通，才执行打印程序
//                    //                    LogUtil.e("ping(ip)", ping(ip) + "");
//                    if (ping(ip)) {
//                        sleep(800);         //睡 1 秒
//                        try {
//                            pos = new Pos(ip, 9100, "GBK");    //第一个参数是打印机网口IP
//                            for (FoodsDetail foodsDetail : listfoodsDetailPress) {
//                                if (category_number.contains("," + foodsDetail.getCategory_id() + ",")) {
//                                    if ("0".equals(kit_type)) {
//                                        if (isFirstPrint) {
//                                            //初始化打印机
//                                            pos.initPos();
//                                            pos.fontSize(3);        //加大“催菜单”三字
//                                            //如果有换桌操作，则显示原桌号
//                                            if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                                pos.printTextNewLine("转台通知");
//                                                noChangeTable = false;   //改过桌号的标识
//                                            } else {
//                                                pos.printTextNewLine("催菜单");
//                                            }
//                                            pos.fontSize(2);
//                                            pos.printTextNewLine("----------------");
//                                            //如果订单序号不为空，则打印订单序号
//                                            if (isDaySn) {
//                                                pos.printTextNewLine("订单序号：");
//                                                pos.printText(day_sn);
//                                            }
//                                            pos.printTextNewLine("桌号：");
//                                            pos.printText(table_no);
//                                            //如果有换桌操作，则显示原桌号,没有的话则直接打印时间就OK了
//                                            if (noChangeTable) {
//                                                pos.printTextNewLine("时间：");
//                                                pos.printText(time);
//                                            } else {
//                                                pos.printTextNewLine("原桌号：");
//                                                pos.printText(before_table);
//                                            }
//                                            pos.printTextNewLine("----------------");
//                                            //不再是第一次打印，接着上面的东西不用打印了
//                                            //                                printMachineBean.setIsFirstPrint(false);
//                                            isFirstPrint = false;
//                                        }
//                                        if (noChangeTable) {
//                                            pos.printTextNewLine(foodsDetail.getFood_name());
//                                            pos.printLocation(20, 1);
//                                            pos.printText("x");
//                                            pos.printLocation(70, 1);
//                                            pos.printText(foodsDetail.getQty());
//                                            pos.printWordSpace(1);
//                                            pos.printText(foodsDetail.getRemark());
//                                        }
//                                        isHavePrint = true;
//                                    } else {
//                                        //初始化打印机
//                                        pos.initPos();
//                                        pos.fontSize(3);        //加大“催菜单”三字
//                                        //如果有换桌操作，则显示原桌号
//                                        if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                                            pos.printTextNewLine("转台通知");
//                                            noChangeTable = false;   //改过桌号的标识
//                                        } else {
//                                            pos.printTextNewLine("催菜单");
//                                        }
//                                        pos.fontSize(2);
//                                        pos.printTextNewLine("----------------");
//                                        //如果订单序号不为空，则打印订单序号
//                                        if (isDaySn) {
//                                            pos.printTextNewLine("订单序号：");
//                                            pos.printText(day_sn);
//                                        }
//                                        pos.printTextNewLine("桌号：");
//                                        pos.printText(table_no);
//                                        //如果有换桌操作，则显示原桌号,没有的话则直接打印时间就OK了
//                                        if (noChangeTable) {
//                                            pos.printTextNewLine("时间：");
//                                            pos.printText(time);
//                                        } else {
//                                            pos.printTextNewLine("原桌号：");
//                                            pos.printText(before_table);
//                                        }
//                                        pos.printTextNewLine("----------------");
//                                        if (noChangeTable) {
//                                            pos.printTextNewLine(foodsDetail.getFood_name());
//                                            pos.printLocation(20, 1);
//                                            pos.printText("x");
//                                            pos.printLocation(70, 1);
//                                            pos.printText(foodsDetail.getQty());
//                                            pos.printWordSpace(1);
//                                            pos.printText(foodsDetail.getRemark());
//                                        }
//                                        //防止内容没有打印是包异常
//                                        pos.printLine(3);
//                                        //切纸
//                                        pos.feedAndCut();
//                                    }
//                                }
//                            }
//                            if (isHavePrint) {
//                                //防止内容没有打印是包异常
//                                pos.printLine(3);
//                                //切纸
//                                pos.feedAndCut();
//                            }
//                            if (pos != null) {
//                                //关闭打印IO流
//                                pos.closeIOAndSocket();
//                            }
//                        } catch (UnknownHostException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "UnknownHostException");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            LogUtil.e("PRINT_ERROR", "IOException");
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    /**
//     * 打印前台票
//     */
//    private void printTicket() {
//        //对前台打印机进行轮询，找到打印机ip
//        for (PrintMachineBean printMachineBean : listPrintMachine) {
//            if ("0".equals(printMachineBean.getType())) {
//                if ("0".equals(printMachineBean.getWidth())) {
//                    //打印纸是80
//                    startPrintTicket(printMachineBean.getIp());
//                } else {
//                    //打印纸是58
//                    startPrintTicket58(printMachineBean.getIp());
//                }
//            }
//        }
//    }
//
//    /**
//     * 58打印纸打印小票（纸有点小）
//     *
//     * @param ip
//     */
//    private void startPrintTicket58(String ip) {
//        try {
//            if (ping(ip)) {
//                sleep(800);
//                try {
//                    Pos pos = new Pos(ip, 9100, "GBK");    //第一个参数是打印机网口IP
//                    //初始化打印机
//                    pos.initPos();
//                    pos.bold(true);
//                    //                    pos.printTabSpace(2);
//                    //                    pos.printWordSpace(1);
//                    pos.printLocation(1);   //居中打印
//                    pos.printText(shopName);
//                    pos.printLocation(0);   //居左打印
//                    pos.printTextNewLine("--------------------------------");
//                    pos.bold(false);
//                    //如果订单序号不为空，则打印订单序号
//                    if (isDaySn) {
//                        pos.printTextNewLine("订单序号：");
//                        pos.printText(day_sn);
//                    }
//                    pos.printTextNewLine("订 单 号：");
//                    pos.printText(order_id + "");
//                    pos.printTextNewLine("桌    号：");
//                    pos.printText(table_no);
//                    //如果有换桌操作，则显示原桌号
//                    if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                        pos.bold(true);
//                        pos.printTextNewLine("转台备注: 原桌号 ");
//                        pos.printText(before_table);
//                        pos.bold(false);
//                    }
//                    //            pos.printTextNewLine("订单状态：订单已确认");
//                    pos.printTextNewLine("订单日期：");
//                    pos.printText(date_time);
//                    if (!pay_by.isEmpty()) {
//                        pos.printTextNewLine("付 款 人：");
//                        pos.printText(pay_by);
//                    }
//                    pos.printTextNewLine("服 务 员：");
//                    pos.printText(waiterName);
//                    pos.printTextNewLine("订单备注：");
//                    if (!orderRemark.isEmpty() && !"null".equals(orderRemark)) {
//                        pos.printText(orderRemark);
//                    } else {
//                        pos.printText("");
//                    }
//                    pos.printLine(2);
//                    pos.printText("品项");
//                    pos.printLocation(126, 0);
//                    pos.printText("单价");
//                    pos.printLocation(0, 1);
//                    //                    pos.printWordSpace(1);
//                    pos.printText("数量");
//                    pos.printLocation(50, 1);
//                    //                    pos.printWordSpace(2);
//                    pos.printText("小计");
//                    pos.printTextNewLine("- - - - - - - - - - - - - - - -");
//                    //点餐
//                    if (listfoodsDetailTicket != null && listfoodsDetailTicket.size() > 0) {
//                        for (FoodsDetail foodsDetail : listfoodsDetailTicket) {
//                            pos.printTextNewLine(foodsDetail.getFood_name());
//                            pos.printLocation(126, 0);
//                            pos.printWordSpace(1);
//                            pos.printText(foodsDetail.getPrice());
//                            pos.printLocation(0, 1);
//                            pos.printText(" " + foodsDetail.getQty());
//                            pos.printLocation(50, 1);
//                            pos.printText(foodsDetail.getSubtotal());
//                        }
//                    }
//                    //加菜
//                    if (listfoodsDetailAddTicket != null && listfoodsDetailAddTicket.size() > 0) {
//                        pos.printTextNewLine("- - - - - - - - - - - - - - - -");
//                        pos.printTextNewLine("加    菜");
//                        pos.printLine(1);
//                        for (FoodsDetail foodsDetail : listfoodsDetailAddTicket) {
//                            pos.printTextNewLine(foodsDetail.getFood_name());
//                            pos.printLocation(126, 0);
//                            pos.printWordSpace(1);
//                            pos.printText(foodsDetail.getPrice());
//                            pos.printLocation(0, 1);
//                            pos.printText(" " + foodsDetail.getQty());
//                            pos.printLocation(50, 1);
//                            pos.printText(foodsDetail.getSubtotal());
//                        }
//                    }
//                    //退菜
//                    if (listfoodsDetailRetreatTicket != null && listfoodsDetailRetreatTicket.size() > 0) {
//                        pos.printTextNewLine("- - - - - - - - - - - - - - - -");
//                        pos.printTextNewLine("退    菜");
//                        pos.printLine(1);
//                        for (FoodsDetail foodsDetail : listfoodsDetailRetreatTicket) {
//                            pos.printTextNewLine(foodsDetail.getFood_name());
//                            pos.printLocation(126, 0);
//                            pos.printText("");
//                            pos.printLocation(0, 1);
//                            pos.printText("-" + foodsDetail.getRetreat());
//                            pos.printLocation(50, 1);
//                            pos.printText(foodsDetail.getRetreatMoney());
//                        }
//                    }
//                    pos.printTextNewLine("- - - - - - - - - - - - - - - -");
//                    pos.printTextNewLine("合    计：");
//                    pos.printText(cost + "");
//                    if (pay_state == 0) {
//                        pos.printTextNewLine("支付状态：未支付");
//                    } else {
//                        pos.printTextNewLine("实际支付：");
//                        pos.printText(amount + "");
//                        pos.printTextNewLine("支付状态：已支付");
//                    }
//                    //            pos.printLine(1);
//                    pos.printTextNewLine("--------------------------------");
//                    pos.printLocation(1);   //居中打印
//                    pos.printTextNewLine("欢迎下次光临");
//                    pos.printLine(3);
//                    //打印二维码
//                    //            pos.qrCode("http://blog.csdn.net/haovip123");
//                    //切纸
//                    pos.feedAndCut();
//                    pos.closeIOAndSocket();
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                    LogUtil.e("PRINT_ERROR", "UnknownHostException");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    LogUtil.e("PRINT_ERROR", "IOException");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 根据打印机IP开始进行前台打印
//     *
//     * @param ip
//     */
//    private void startPrintTicket(String ip) {
//        try {
//            if (ping(ip)) {
//                sleep(800);
//                try {
//                    Pos pos = new Pos(ip, 9100, "GBK");    //第一个参数是打印机网口IP
//                    //初始化打印机
//                    pos.initPos();
//                    pos.bold(true);
//                    pos.printTabSpace(2);
//                    pos.printWordSpace(1);
//                    pos.printText(shopName);
//                    //                    pos.printLocation(0);   //居左打印
//                    pos.printTextNewLine("----------------------------------------------");
//                    pos.bold(false);
//                    //如果订单序号不为空，则打印订单序号
//                    if (isDaySn) {
//                        pos.printTextNewLine("订单序号：");
//                        pos.printText(day_sn);
//                    }
//                    pos.printTextNewLine("订 单 号：");
//                    pos.printText(order_id + "");
//                    pos.printTextNewLine("桌    号：");
//                    pos.printText(table_no);
//                    //如果有换桌操作，则显示原桌号
//                    if (!before_table.isEmpty() && !"null".equals(before_table)) {
//                        pos.bold(true);
//                        pos.printTextNewLine("转台备注: 原桌号 ");
//                        pos.printText(before_table);
//                        pos.bold(false);
//                    }
//                    //            pos.printTextNewLine("订单状态：订单已确认");
//                    pos.printTextNewLine("订单日期：");
//                    pos.printText(date_time);
//                    if (!pay_by.isEmpty()) {
//                        pos.printTextNewLine("付 款 人：");
//                        pos.printText(pay_by);
//                    }
//                    pos.printTextNewLine("服 务 员：");
//                    pos.printText(waiterName);
//                    pos.printTextNewLine("订单备注：");
//                    if (!orderRemark.isEmpty() && !"null".equals(orderRemark)) {
//                        pos.printText(orderRemark);
//                    } else {
//                        pos.printText("");
//                    }
//                    pos.printLine(2);
//                    pos.printText("品项");
//                    pos.printLocation(20, 1);
//                    pos.printText("单价");
//                    pos.printLocation(99, 1);
//                    pos.printWordSpace(1);
//                    pos.printText("数量");
//                    pos.printWordSpace(2);
//                    pos.printText("小计");
//                    pos.printTextNewLine("- - - - - - - - - - - - - - - - - - - - - - -");
//                    //点餐
//                    if (listfoodsDetailTicket != null && listfoodsDetailTicket.size() > 0) {
//                        for (FoodsDetail foodsDetail : listfoodsDetailTicket) {
//                            pos.printTextNewLine(foodsDetail.getFood_name());
//                            pos.printLocation(20, 1);
//                            pos.printText(foodsDetail.getPrice());
//                            pos.printLocation(99, 1);
//                            pos.printWordSpace(1);
//                            pos.printText(" " + foodsDetail.getQty());
//                            pos.printWordSpace(3);
//                            pos.printText(foodsDetail.getSubtotal());
//                        }
//                    }
//                    //加菜
//                    if (listfoodsDetailAddTicket != null && listfoodsDetailAddTicket.size() > 0) {
//                        pos.printTextNewLine("- - - - - - - - - - - - - - - - - - - - - - -");
//                        pos.printTextNewLine("加    菜");
//                        pos.printLine(1);
//                        for (FoodsDetail foodsDetail : listfoodsDetailAddTicket) {
//                            pos.printTextNewLine(foodsDetail.getFood_name());
//                            pos.printLocation(20, 1);
//                            pos.printText(foodsDetail.getPrice());
//                            pos.printLocation(99, 1);
//                            pos.printWordSpace(1);
//                            pos.printText(" " + foodsDetail.getQty());
//                            pos.printWordSpace(3);
//                            pos.printText(foodsDetail.getSubtotal());
//                        }
//                    }
//                    //退菜
//                    if (listfoodsDetailRetreatTicket != null && listfoodsDetailRetreatTicket.size() > 0) {
//                        pos.printTextNewLine("- - - - - - - - - - - - - - - - - - - - - - -");
//                        pos.printTextNewLine("退    菜");
//                        pos.printLine(1);
//                        for (FoodsDetail foodsDetail : listfoodsDetailRetreatTicket) {
//                            pos.printTextNewLine(foodsDetail.getFood_name());
//                            pos.printLocation(20, 1);
//                            pos.printText("");
//                            pos.printLocation(99, 1);
//                            pos.printWordSpace(1);
//                            pos.printText("-" + foodsDetail.getRetreat());
//                            pos.printWordSpace(3);
//                            pos.printText(foodsDetail.getRetreatMoney());
//                        }
//                    }
//                    pos.printTextNewLine("- - - - - - - - - - - - - - - - - - - - - - -");
//                    pos.printTextNewLine("合    计：");
//                    pos.printText(cost + "");
//                    if (pay_state == 0) {
//                        pos.printTextNewLine("支付状态：未支付");
//                    } else {
//                        pos.printTextNewLine("实际支付：");
//                        pos.printText(amount + "");
//                        pos.printTextNewLine("支付状态：已支付");
//                    }
//                    //            pos.printLine(1);
//                    pos.printTextNewLine("----------------------------------------------");
//                    pos.printLocation(1);   //居中打印
//                    pos.printTextNewLine("欢迎下次光临");
//                    pos.printLine(1);
//                    //打印二维码
//                    //            pos.qrCode("http://blog.csdn.net/haovip123");
//                    //切纸
//                    pos.feedAndCut();
//                    pos.closeIOAndSocket();
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                    LogUtil.e("PRINT_ERROR", "UnknownHostException");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    LogUtil.e("PRINT_ERROR", "IOException");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 把已经打印过的食物的id放在数组中，post到服务器
//     */
//    private void postPrintedFoodId(String ids, String strRetreat) {
//        //token, ids, type, order_id, retreat = null
//        if (!"".equals(ids)) {      //打印的订单ids不为空
//            ids = ids.substring(0, ids.length() - 1);
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("token", SharedTools.getToken(BaseApplication.getContext()));
//            params.put("ids", ids);
//            params.put("type", type);
//            params.put("order_id", order_id + "");
//            params.put("isWaiter", "1");
//            if (!"".equals(strRetreat)) {
//                strRetreat = strRetreat.substring(0, strRetreat.length() - 1);
//                params.put("retreat", strRetreat);
//            }
//            LogUtil.e("strRetreat", "-----" + strRetreat + "-----");
//            Net.getInstance().postUpdatePrint(params, new Callback() {
//                @Override
//                public void success(String json) {
//                    LogUtil.e("isPrintFoodTag", json);
//                }
//
//                @Override
//                public void notice(JSONObject jsonObject, String message) {
//                }
//
//                @Override
//                public void failed(String e) {
//                }
//            });
//        }
//    }
//
//    /**
//     * 睡几秒
//     */
//    private void sleep(int time) {
//        try {
//            Thread.sleep(time);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //Ping IP 是否能ping通
//    private boolean ping(String ipAddress) throws Exception {
//        int timeOut = 3000;  //超时应该在3钞以上
//        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
//        return status;
//    }
//}