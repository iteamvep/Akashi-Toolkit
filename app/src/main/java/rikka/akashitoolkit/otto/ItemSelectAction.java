package rikka.akashitoolkit.otto;

/**
 * Created by Rikka on 2016/7/31.
 */
public class ItemSelectAction {

    public static class StartShip {

        private int position;

        public StartShip(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public static class StartEquip {

        private int position;
        private int slot;

        public StartEquip(int position, int slot) {
            this.position = position;
            this.slot = slot;
        }

        public int getPosition() {
            return position;
        }

        public int getSlot() {
            return slot;
        }
    }

    public static class Finish {

        private int id;

        public Finish(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

}
