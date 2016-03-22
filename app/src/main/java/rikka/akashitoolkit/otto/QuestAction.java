package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/3/22.
 */
public class QuestAction {
    public static class FilterChanged {
        private int flag;

        public FilterChanged(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }

    public static class KeywordChanged {
        private String keyword;

        public KeywordChanged(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }

}
