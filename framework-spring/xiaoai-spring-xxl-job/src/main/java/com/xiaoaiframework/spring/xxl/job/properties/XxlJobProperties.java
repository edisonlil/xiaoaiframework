package com.xiaoaiframework.spring.xxl.job.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     * 执行器通讯TOKEN [选填]：非空时启用；
     */
    private String accessToken;

    /**
     * admin配置
     */
    private Admin admin;

    /**
     * 执行器配置
     */
    private Executor executor;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    public static class Admin{

        /**
         * 调度中心部署跟地址
         * [选填]：如调度中心集群部署存在多个地址则用逗号分隔。
         * 执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
         */
        private String addresses;

        public String getAddresses() {
            return addresses;
        }

        public void setAddresses(String addresses) {
            this.addresses = addresses;
        }
    }

    public static class Executor{

        /**
         * 执行器AppName
         * [选填]：执行器心跳注册分组依据；为空则关闭自动注册
         */
        private String appname;

        /**
         * 执行器注册
         * [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
         */
        @Deprecated
        private String address;


        /**
         * 执行器IP
         * [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；
         * 地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
         */
        private String ip;

        /**
         * 执行器端口号
         * [选填]：小于等于0则自动获取；默认端口为9999，
         * 单机部署多个执行器时，注意要配置不同执行器端口；
         */
        private Integer port = 9999;

        /**
         * 执行器运行日志文件存储磁盘路径
         * [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
         */
        private String logpath;

        /**
         * 执行器日志文件保存天数
         * [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
         */
        private Integer logretentiondays = 7;


        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getLogpath() {
            return logpath;
        }

        public void setLogpath(String logpath) {
            this.logpath = logpath;
        }

        public Integer getLogretentiondays() {
            return logretentiondays;
        }

        public void setLogretentiondays(Integer logretentiondays) {
            this.logretentiondays = logretentiondays;
        }
    }


    public static XxlJobProperties build(){

        XxlJobProperties xxlJobProperties = new XxlJobProperties();
        xxlJobProperties.setAdmin(new Admin());
        xxlJobProperties.setExecutor(new Executor());
        return xxlJobProperties;
    }




}
