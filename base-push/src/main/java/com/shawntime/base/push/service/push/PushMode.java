package com.shawntime.base.push.service.push;

/**
 * 推荐模式
 */
public class PushMode {

    /**
     * 二进制第1位是否允许全量推送更新，0表示否，1表示是
     * 1 << 0 = 0001  = 1
     */
    public static final int ALLOW_ALL_UPDATE = 1 << 0;

    /**
     * 二进制第2位是否允许全量下线，0表示否，1表示是
     * 1 << 1 = 0010  = 2
     */
    public static final int ALLOW_ALL_DOWN_LINE = 1 << 1;

    /**
     * 二进制第3位是否允许并发推送，0表示否，1表示是
     * 1 << 2 = 0100 = 4
     */
    public static final int ALLOW_CURRENCY = 1 << 2;

    /**
     * 二进制第4位是否允许日志入库，0表示否，1表示是
     * 1 << 3 = 1000 = 8
     */
    public static final int ALLOW_LOG_TO_DB = 1 << 3;


    /**
     * 目前所拥有的权限状态
     */
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * 新增1个或者多个权限
     * 按位或，有1为1，全0为0，按位或后有1的位置全都有了权限
     */
    public void addMode(int flag) {
        this.flag = this.flag | flag;
    }

    /**
     * 删除1个或多个权限
     * 先按位取反，这样不需要删除的位置为1，删除的位置为0，在按位与，将对于为0的位置置0
     *
     */
    public void removeMode(int flag) {
        this.flag = this.flag & ~flag;
    }

    /**
     * 是否有某个操作的权限
     * 先按位与后如果操作位对应的是1，则操作位继续为1，否则全为0，得到的结果要么为flag，要么全为0
     */
    public boolean hasMode(int flag) {
        return (this.flag & flag) == flag;
    }

    public boolean hasNotMode(int flag) {
        return (this.flag & flag) == 0;
    }

}
