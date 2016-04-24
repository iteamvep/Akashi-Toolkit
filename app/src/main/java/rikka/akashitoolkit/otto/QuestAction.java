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

    public static class JumpToQuest {
        private int index;
        private int type;

        public JumpToQuest(int type, int index) {
            this.index = index;
            this.type = type;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class JumpedToQuest {
        public JumpedToQuest() {
        }
    }

    public static class IsSearchingChanged {
        private boolean searching;

        public IsSearchingChanged(boolean searching) {
            this.searching = searching;
        }

        public boolean isSearching() {
            return searching;
        }

        public void setSearching(boolean searching) {
            this.searching = searching;
        }
    }
}
