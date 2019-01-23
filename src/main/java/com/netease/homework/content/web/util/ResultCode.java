package com.netease.homework.content.web.util;

/**
 * <pre>
 * 返回码，用于描述通用的错误类型，这种错误类型和用例无关，即：它可以被使用于多个用例。
 * 更新这个类时，需要同时更新base.js中的同名对象。
 * </pre>
 *
 * @author [[mailto:hzmaoyinjie@corp.netease.com][Mao Yinjie]]
 */
public interface ResultCode {

    /** 未登录 */
    int ERROR_NOT_LOGGED_IN = -999;

    /** 不允许URS帐号 */
    int ERROR_URS_NOT_ALLOWED = -998;

    /** 邮箱名称格式错误 */
    int ERROR_MAILBOX_FORMAT_ERROR = -979;

    /** 不允许重复操作 */
    int ERROR_ACTION_REPEATED = -799;

    /** 缺少参数 */
    int ERROR_PARAMETER_MISSING = -789;

    /** 必须使用POST请求 */
    int ERROR_HTTP_POST_REQUIRED = -779;

    /** 页码超出 */
    int ERROR_PAGE_LIMIT_EXCEEDED = -769;

    /** 不支持的源类型 */
    int ERROR_SOURCE_TYPE_NOT_SUPPORTED = -759;

    /** 未找到源类目 */
    int ERROR_SOURCE_CATEGORY_NOT_FOUND = -758;

    /** 参数格式错误 */
    int ERROR_BAD_PARAMETER = -749;

    /** 部分逻辑错误 */
    int ERROR_PARTIAL_FAILURE = -2;

    /** 未知错误/服务器异常 */
    int ERROR_UNKNOWN = -1;

    /** 操作成功 */
    int SUCCESS = 0;

    // 0 ~ +infinity: PRIS后台的返回码，具体见后台协议文档。PRIS后台的返回码为0时，也表示操作成功。

    static interface Trade {
        int ERROR_NO_ITEMS_LEFT = -1101;

        int ERROR_INVALID_PACKAGE = -1102;

        int ERROR_VALIDATE_QUAN = -1103;

        int ERROR_VALIDATE_HONGBAO = -1104;

        int ERROR_NEEDS_RECHARGE = -1105;

        int ERROR_FREELIMIT_OUTDATED = -1106;

        int ERROR_NO_ARTICLES_NEED_TO_PAY = -1107;

        int ERROR_BOOK_ALREADY_PAID = -1108;

        int ERROR_BOOK_ALREADY_IN_CART = -1109;

        int ERROR_TRADE_OUTDATED = -1110;

        int ERROR_BOOK_OUTDATED = -1111;
    }

    static interface Cloud {

        int BOOK_NOT_EXIST = -1201;

        int NOT_ENOUGH_SPACE = -1202;

        int NOT_ENOUGH_SCORE = -1203;

        int SHOW_TIP = -1204;

    }

    static interface Grant {

        int ERROR_CODE_INVALID = -1301;

        int ERROR_CAN_NOT_GET_MORE = -1302;

        int ERROR_CODE_USED = -1303;

        int ERROR_OUTDATED = -1304;

        int ERROR_DEVICE_CAN_NOT_GET_MORE = -1305;

        int ERROR_FAILED = -1306;

        int ERROR_GRANTED = -1307;

        int ERROR_BOOK_PURCHASED = -1308;

        int ERROR_UNSUPPORT_TYPE = -1309;

        int ERROR_USER = -1310;

        int ERROR_UNMET_CONDITION = -1311;

        int ERROR_COUNT_OUT = -1312;
    }

    static interface LOGIN {

        /** 手机账号不存在，不需要图片验证 */
        int ERROR_NOT_REGISTERED = -1401;

        /** 密码错误，不需要图片验证 */
        int AUTHENTICATION_FAILED = -1402;

        /** 手机账号不存在，需要图片验证 */
        int NOT_REGISTERED_NEED_CODE = -1403;

        /** 密码错误，需要图片验证 */
        int AUTH_FAIL_NEED_CODE = -1404;

        /** 图片验证码错误 */
        int ERROR_CAPTCHA = -1405;
    }

    static interface PRESENT {
        int ERROR_NEEDS_RECHARGE = -1501;
    }

    static interface Activity {
        /** 未中奖 */
        int UNLUCKY = -1601;

        /** 活动不存在 */
        int NOT_EXISTED = -1602;

        /** 中奖记录不存在或已兑换 */
        int NO_RECORD_OR_EXCHANGED = -1603;

        /** 摇奖积分不够 */
        int NOT_ENOUGH_POINT = -1604;

        /** 摇奖次数限制,可以通过分享换取摇奖次数 */
        int DAY_SLOT_TIME_LIMIT = -1605;

        /** 达到一天摇奖上限 */
        int DAY_MAX_SLOT_TIME_LIMIT = -1606;

        int TOTAL_SLOT_TIME_LIMIT = -1607;

        /** 设备达到一天摇奖上限 */
        int DAY_MAX_SLOT_TIME_LIMIT_DEVICE = -1608;

        /** 已兑换 */
        int EXCHANGED = -1609;

        /** 金额错误 */
        int AMOUNT_INVALID = -1610;

        /** 金额不足 */
        int AMOUNT_LESS = -1611;
    }

    static interface VerifyCode {

        int FORBIDDEN = -1701;

        int NEED_PHONE_LOGIN = -1702;

        int NEED_CTCC_PHONE = -1703;
    }

    static interface ShareFree {

        /** 已领取 */
        int ALREADY_GOTTEN = -1801;

        /** 活动已过期 */
        int OUTDATE = -1802;

        /** 领取失败 */
        int GET_FAIL = -1803;
    }

    static interface Comment {
        /** 设置了评论黑名单 */
        int ERROR_COMMENT_BLACKLIST = -1901;
    }

    static interface Register {
        /** 账号已存在 */
        int ACCOUNT_EXIST = -2001;

        /** 账号不存在，更改密码时候 */
        int ACCOUNT_NOT_EXIST = -2002;

        /** 验证码校验失败 */
        int VERIFY_CODE_FAIL = -2003;
    }

    static interface WeekSign {
        /** 已签到 */
        int SIGNED_IN = -2101;

        /** 补签次数不足 */
        int NO_RECOVER_TIME = -2102;
    }

    static interface Recharge {
        /** 图形验证码错误 */
        int ERROR_CAPTCHA = -2201;

        /** 手机验证码错误 */
        int MOBILE_ERROR_CAPTCHA = -2202;

        /** 将军令动态密码错误 */
        int MOBILE_ERROR_OPTPWD = -2203;

        /** 密保卡密码错误 */
        int MOBILE_ERROR_PPCPWD = -2204;

        int ERROR_IAPRECHARGE_FAIL = -2205;
    }

    static interface Vote {
        /** 签名校验失败 */
        int SIGNATURE_MISMATCH = -3001;

        /** 重复操作 */
        int REPEATED_ACTION = -3002;

        /** 该时间段不允许投票 */
        int ERROR_TIME_RANGE = -3003;

        /** 不允许投票 */
        int FORBIDDEN = -3004;
    }

    static interface Mall {
        /** 商品不可用 */
        int GOOD_UNAVAILABLE = -4001;

        /** 用户今日兑换达到上限 */
        int USER_DAILY_EXCHANGE_LIMIT = -4002;

        /** 商品今日兑换达到上限 */
        int GOOD_DAILY_EXCHANGE_LIMIT = -4003;

        /** 积分不足 */
        int USER_BALANCE_LESS = -4004;

        /** 验证码 */
        int NEED_CHECK_CODE = -4005;
    }

    static interface INVITE {
        /** 电话号码不对 */
        int PHONE_ERROR = -5001;

        /** 邮箱地址不对 */
        int EMAIL_ERROR = -5002;
    }

    static interface TOKEN {
        int INVALID_DEVICE_KEY = -6001;
        int TOKEN_EXPIRED = -6002;
        int INVALID_TOKEN = -6003;
    }

    static interface DailySign {
        int ALREADY_SIGNED = -7001;
        // 补签次数不足
        int NO_RECOVER_TIME = -7002;
        // 礼包已经领取过了
        int ALREADY_GET_BONUS = -7003;
    }

}

