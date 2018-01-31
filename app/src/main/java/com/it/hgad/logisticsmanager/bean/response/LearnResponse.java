package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/6/8.
 */
public class LearnResponse extends BaseReponse {

    /**
     * data : {"perpage":-1,"recordcount":3,"firstpage":1,"pagecount":-3,"currentpage":1,"lastpage":-3,"listcount":3,"nextpage":-3,"dyntitles":null,"listdata":[{"createTime":"2017-06-09T10:11:31","author":"的","clickNum":"0","id":23,"title":"天天团","content":"<p>胜多负少水电费<\/p><p><br/><\/p><p><br/><\/p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;暗室逢灯按时的<\/p><p><\/p><p><br/><\/p><p><br/><\/p><p><br/><\/p><p><br/><\/p>","url":"files/article/20170609101130_天天团.html"},{"createTime":"2017-06-09T09:47:56","author":"是否","clickNum":"0","id":24,"title":"方法","content":"<p>安抚按时f<\/p><p><br/><\/p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;阿斯蒂芬阿斯蒂芬<\/p><p><\/p>","url":"files/article/20170609094755_方法.html"},{"createTime":"2017-06-09T09:57:51","author":"aaa","clickNum":"0","id":25,"title":"ttt","content":"<pstyle=\"text-align:center;line-height:25px\"><strong><spanstyle=\"font-size:19px;font-family:宋体;color:black;letter-spacing:4px\">工作联系函<\/span><\/strong><strong><\/strong><\/p><pstyle=\"text-align:center;line-height:25px\"><strong><spanstyle=\"font-size:16px;font-family:宋体;color:black;letter-spacing:4px\">&nbsp;<\/span><\/strong><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">项目名称：协和西院工程信息自动化系统<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span>编号：<span>17\u201401<\/span><\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">&nbsp;<\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">致：<span>&nbsp;<\/span><spanstyle=\"text-decoration:underline;\">华科天喻信息公司夏经理<\/span><\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">提报人：<spanstyle=\"text-decoration:underline;\">武汉同济物业管理有限公司<span>&nbsp;<\/span>协和西院项目部<\/span><\/span><spanstyle=\"font-size:16px;font-family:宋体;color:red\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">电话：<spanstyle=\"text-decoration:underline;\"><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span><\/span><\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">主题：<\/span><spanstyle=\"text-decoration:underline;\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">关于工程信息自动化系统需要解决的方案<\/span><\/span><spanstyle=\"text-decoration:underline;\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">建议函<\/span><\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">页码<span>:<spanstyle=\"text-decoration:underline;\">_&nbsp;<\/span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span>时间：<spanstyle=\"text-decoration:underline;\"><span>2017<\/span><\/span>年<span>4<\/span><spanstyle=\"text-decoration:underline;\">月<span>7<\/span><\/span>日<spanstyle=\"text-decoration:underline;\"><\/span><\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">呈送：<spanstyle=\"text-decoration:underline;\">华科天喻相关领导<\/span><\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体\">&nbsp;<\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">紧急<\/span><spanstyle=\"font-size:16px;font-family:Wingdings;color:black\">þ<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">非紧急□请答复<\/span><spanstyle=\"font-size:16px;font-family:Wingdings;color:black\">þ<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">请办理□<\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">联系工作内容：<\/span><\/p><pstyle=\"line-height:150%\"><spanstyle=\"font-size:16px;line-height:150%;font-family:宋体;color:black\">&nbsp;<\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">&nbsp;&nbsp;<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">关于工程信息自动化系统需要解决的方案。<\/span><\/p><p><br/><\/p>","url":"files/article/20170609095751_ttt.html"}],"prevpage":1,"dyncolsdata":null,"oddsize":false}
     */
    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public class DataEntity {
        /**
         * perpage : -1
         * recordcount : 3
         * firstpage : 1
         * pagecount : -3
         * currentpage : 1
         * lastpage : -3
         * listcount : 3
         * nextpage : -3
         * dyntitles : null
         * listdata : [{"createTime":"2017-06-09T10:11:31","author":"的","clickNum":"0","id":23,"title":"天天团","content":"<p>胜多负少水电费<\/p><p><br/><\/p><p><br/><\/p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;暗室逢灯按时的<\/p><p><\/p><p><br/><\/p><p><br/><\/p><p><br/><\/p><p><br/><\/p>","url":"files/article/20170609101130_天天团.html"},{"createTime":"2017-06-09T09:47:56","author":"是否","clickNum":"0","id":24,"title":"方法","content":"<p>安抚按时f<\/p><p><br/><\/p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;阿斯蒂芬阿斯蒂芬<\/p><p><\/p>","url":"files/article/20170609094755_方法.html"},{"createTime":"2017-06-09T09:57:51","author":"aaa","clickNum":"0","id":25,"title":"ttt","content":"<pstyle=\"text-align:center;line-height:25px\"><strong><spanstyle=\"font-size:19px;font-family:宋体;color:black;letter-spacing:4px\">工作联系函<\/span><\/strong><strong><\/strong><\/p><pstyle=\"text-align:center;line-height:25px\"><strong><spanstyle=\"font-size:16px;font-family:宋体;color:black;letter-spacing:4px\">&nbsp;<\/span><\/strong><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">项目名称：协和西院工程信息自动化系统<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span>编号：<span>17\u201401<\/span><\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">&nbsp;<\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">致：<span>&nbsp;<\/span><spanstyle=\"text-decoration:underline;\">华科天喻信息公司夏经理<\/span><\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">提报人：<spanstyle=\"text-decoration:underline;\">武汉同济物业管理有限公司<span>&nbsp;<\/span>协和西院项目部<\/span><\/span><spanstyle=\"font-size:16px;font-family:宋体;color:red\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">电话：<spanstyle=\"text-decoration:underline;\"><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span><\/span><\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">主题：<\/span><spanstyle=\"text-decoration:underline;\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">关于工程信息自动化系统需要解决的方案<\/span><\/span><spanstyle=\"text-decoration:underline;\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">建议函<\/span><\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">页码<span>:<spanstyle=\"text-decoration:underline;\">_&nbsp;<\/span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<\/span>时间：<spanstyle=\"text-decoration:underline;\"><span>2017<\/span><\/span>年<span>4<\/span><spanstyle=\"text-decoration:underline;\">月<span>7<\/span><\/span>日<spanstyle=\"text-decoration:underline;\"><\/span><\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">呈送：<spanstyle=\"text-decoration:underline;\">华科天喻相关领导<\/span><\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体\">&nbsp;<\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">紧急<\/span><spanstyle=\"font-size:16px;font-family:Wingdings;color:black\">þ<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">非紧急□请答复<\/span><spanstyle=\"font-size:16px;font-family:Wingdings;color:black\">þ<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">请办理□<\/span><\/p><pstyle=\"line-height:18px\"><spanstyle=\"font-size:16px;font-family:宋体;color:black\">联系工作内容：<\/span><\/p><pstyle=\"line-height:150%\"><spanstyle=\"font-size:16px;line-height:150%;font-family:宋体;color:black\">&nbsp;<\/span><\/p><p><spanstyle=\"font-size:16px;font-family:宋体;color:black\">&nbsp;&nbsp;<\/span><spanstyle=\"font-size:16px;font-family:宋体;color:black\">关于工程信息自动化系统需要解决的方案。<\/span><\/p><p><br/><\/p>","url":"files/article/20170609095751_ttt.html"}]
         * prevpage : 1
         * dyncolsdata : null
         * oddsize : false
         */
        private int perpage;
        private int recordcount;
        private int firstpage;
        private int pagecount;
        private int currentpage;
        private int lastpage;
        private int listcount;
        private int nextpage;
        private String dyntitles;
        private List<ListdataEntity> listdata;
        private int prevpage;
        private String dyncolsdata;
        private boolean oddsize;

        public void setPerpage(int perpage) {
            this.perpage = perpage;
        }

        public void setRecordcount(int recordcount) {
            this.recordcount = recordcount;
        }

        public void setFirstpage(int firstpage) {
            this.firstpage = firstpage;
        }

        public void setPagecount(int pagecount) {
            this.pagecount = pagecount;
        }

        public void setCurrentpage(int currentpage) {
            this.currentpage = currentpage;
        }

        public void setLastpage(int lastpage) {
            this.lastpage = lastpage;
        }

        public void setListcount(int listcount) {
            this.listcount = listcount;
        }

        public void setNextpage(int nextpage) {
            this.nextpage = nextpage;
        }

        public void setDyntitles(String dyntitles) {
            this.dyntitles = dyntitles;
        }

        public void setListdata(List<ListdataEntity> listdata) {
            this.listdata = listdata;
        }

        public void setPrevpage(int prevpage) {
            this.prevpage = prevpage;
        }

        public void setDyncolsdata(String dyncolsdata) {
            this.dyncolsdata = dyncolsdata;
        }

        public void setOddsize(boolean oddsize) {
            this.oddsize = oddsize;
        }

        public int getPerpage() {
            return perpage;
        }

        public int getRecordcount() {
            return recordcount;
        }

        public int getFirstpage() {
            return firstpage;
        }

        public int getPagecount() {
            return pagecount;
        }

        public int getCurrentpage() {
            return currentpage;
        }

        public int getLastpage() {
            return lastpage;
        }

        public int getListcount() {
            return listcount;
        }

        public int getNextpage() {
            return nextpage;
        }

        public String getDyntitles() {
            return dyntitles;
        }

        public List<ListdataEntity> getListdata() {
            return listdata;
        }

        public int getPrevpage() {
            return prevpage;
        }

        public String getDyncolsdata() {
            return dyncolsdata;
        }

        public boolean isOddsize() {
            return oddsize;
        }

        public class ListdataEntity {
            /**
             * createTime : 2017-06-09T10:11:31
             * author : 的
             * clickNum : 0
             * id : 23
             * title : 天天团
             * content : <p>胜多负少水电费</p><p><br/></p><p><br/></p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;暗室逢灯按时的</p><p></p><p><br/></p><p><br/></p><p><br/></p><p><br/></p>
             * url : files/article/20170609101130_天天团.html
             */
            private String createTime;
            private String author;
            private String clickNum;
            private int id;
            private String title;
            private String content;
            private String url;

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public void setClickNum(String clickNum) {
                this.clickNum = clickNum;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getCreateTime() {
                return createTime;
            }

            public String getAuthor() {
                return author;
            }

            public String getClickNum() {
                return clickNum;
            }

            public int getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public String getContent() {
                return content;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}
