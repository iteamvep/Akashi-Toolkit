package rikka.akashitoolkit.model;

import java.util.List;

/**
 * Created by Rikka on 2016/4/30.
 */
public class Seasonal {

    /**
     * title : 2016年三周年纪念
     * data : [{"title":"三周年限定立绘","content":[{"type":0,"title":"春の花束mode","file":["KanMusu133IllustThirdAnniversary.png","KanMusu133DmgIllustThirdAnniversary.png","KanMusu135IllustThirdAnniversary.png","KanMusu135DmgIllustThirdAnniversary.png","KanMusu205IllustThirdAnniversary.png","KanMusu205DmgIllustThirdAnniversary.png","KanMusu251IllustThirdAnniversary.png"]},{"type":0,"title":"花语","file":["KanMusu133ThirdAnni-kcwiki.png","KanMusu135ThirdAnni-kcwiki.png","KanMusu205ThirdAnni-kcwiki.png","KanMusu251ThirdAnni-kcwiki.jpg"]}]}]
     */

    private String title;
    /**
     * title : 三周年限定立绘
     * content : [{"type":0,"title":"春の花束mode","file":["KanMusu133IllustThirdAnniversary.png","KanMusu133DmgIllustThirdAnniversary.png","KanMusu135IllustThirdAnniversary.png","KanMusu135DmgIllustThirdAnniversary.png","KanMusu205IllustThirdAnniversary.png","KanMusu205DmgIllustThirdAnniversary.png","KanMusu251IllustThirdAnniversary.png"]},{"type":0,"title":"花语","file":["KanMusu133ThirdAnni-kcwiki.png","KanMusu135ThirdAnni-kcwiki.png","KanMusu205ThirdAnni-kcwiki.png","KanMusu251ThirdAnni-kcwiki.jpg"]}]
     */

    private List<DataEntity> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String title;
        /**
         * type : 0
         * title : 春の花束mode
         * file : ["KanMusu133IllustThirdAnniversary.png","KanMusu133DmgIllustThirdAnniversary.png","KanMusu135IllustThirdAnniversary.png","KanMusu135DmgIllustThirdAnniversary.png","KanMusu205IllustThirdAnniversary.png","KanMusu205DmgIllustThirdAnniversary.png","KanMusu251IllustThirdAnniversary.png"]
         */

        private List<ContentEntity> content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ContentEntity> getContent() {
            return content;
        }

        public void setContent(List<ContentEntity> content) {
            this.content = content;
        }

        public static class ContentEntity {
            private int type;
            private int width;
            private int height;
            private String scale_type;
            private String title;
            private List<String> file;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getSacle_type() {
                return scale_type;
            }

            public void setSacle_type(String scale_type) {
                this.scale_type = scale_type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<String> getFile() {
                return file;
            }

            public void setFile(List<String> file) {
                this.file = file;
            }
        }
    }
}
