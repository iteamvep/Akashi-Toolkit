package com.example.model;

import java.util.List;

/**
 * Created by Rikka on 2016/4/24.
 */
public class Ship2 {


    private int id;
    private int sort_no;
    private String name;
    private String yomi;
    private int stype;
    private int ctype;
    private int cnum;
    private int backs;
    private int after_lv;
    private int after_ship_id;
    private String get_mes;
    private int voice_f;
    private String filename;
    private String book_sinfo;
    /**
     * taik : [13,24]
     * souk : [5,18]
     * houg : [6,29]
     * raig : [18,59]
     * tyku : [7,29]
     * luck : [12,49]
     * soku : 10
     * leng : 1
     * kaih : 37
     * tais : 16
     * slot_num : 2
     * max_eq : [0,0,0,0,0]
     * after_fuel : 100
     * after_bull : 100
     * fuel_max : 15
     * bull_max : 15
     * broken : [1,1,4,0]
     * pow_up : [1,1,0,0]
     * build_time : 18
     */

    private StatsEntity stats;
    private GraphEntity graph;
    private String wiki_id;
    private String chinese_name;
    private List<String> file_version;
    private List<Integer> book_table_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort_no() {
        return sort_no;
    }

    public void setSort_no(int sort_no) {
        this.sort_no = sort_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYomi() {
        return yomi;
    }

    public void setYomi(String yomi) {
        this.yomi = yomi;
    }

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public int getBacks() {
        return backs;
    }

    public void setBacks(int backs) {
        this.backs = backs;
    }

    public int getAfter_lv() {
        return after_lv;
    }

    public void setAfter_lv(int after_lv) {
        this.after_lv = after_lv;
    }

    public int getAfter_ship_id() {
        return after_ship_id;
    }

    public void setAfter_ship_id(int after_ship_id) {
        this.after_ship_id = after_ship_id;
    }

    public String getGet_mes() {
        return get_mes;
    }

    public void setGet_mes(String get_mes) {
        this.get_mes = get_mes;
    }

    public int getVoice_f() {
        return voice_f;
    }

    public void setVoice_f(int voice_f) {
        this.voice_f = voice_f;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBook_sinfo() {
        return book_sinfo;
    }

    public void setBook_sinfo(String book_sinfo) {
        this.book_sinfo = book_sinfo;
    }

    public StatsEntity getStats() {
        return stats;
    }

    public void setStats(StatsEntity stats) {
        this.stats = stats;
    }

    public GraphEntity getGraph() {
        return graph;
    }

    public void setGraph(GraphEntity graph) {
        this.graph = graph;
    }

    public String getWiki_id() {
        return wiki_id;
    }

    public void setWiki_id(String wiki_id) {
        this.wiki_id = wiki_id;
    }

    public String getChinese_name() {
        return chinese_name;
    }

    public void setChinese_name(String chinese_name) {
        this.chinese_name = chinese_name;
    }

    public List<String> getFile_version() {
        return file_version;
    }

    public void setFile_version(List<String> file_version) {
        this.file_version = file_version;
    }

    public List<Integer> getBook_table_id() {
        return book_table_id;
    }

    public void setBook_table_id(List<Integer> book_table_id) {
        this.book_table_id = book_table_id;
    }

    public static class StatsEntity {
        private int soku;
        private int leng;
        private int kaih;
        private int tais;
        private int slot_num;
        private int after_fuel;
        private int after_bull;
        private int fuel_max;
        private int bull_max;
        private int build_time;
        private List<Integer> taik;
        private List<Integer> souk;
        private List<Integer> houg;
        private List<Integer> raig;
        private List<Integer> tyku;
        private List<Integer> luck;
        private List<Integer> max_eq;
        private List<Integer> broken;
        private List<Integer> pow_up;

        public int getSoku() {
            return soku;
        }

        public void setSoku(int soku) {
            this.soku = soku;
        }

        public int getLeng() {
            return leng;
        }

        public void setLeng(int leng) {
            this.leng = leng;
        }

        public int getKaih() {
            return kaih;
        }

        public void setKaih(int kaih) {
            this.kaih = kaih;
        }

        public int getTais() {
            return tais;
        }

        public void setTais(int tais) {
            this.tais = tais;
        }

        public int getSlot_num() {
            return slot_num;
        }

        public void setSlot_num(int slot_num) {
            this.slot_num = slot_num;
        }

        public int getAfter_fuel() {
            return after_fuel;
        }

        public void setAfter_fuel(int after_fuel) {
            this.after_fuel = after_fuel;
        }

        public int getAfter_bull() {
            return after_bull;
        }

        public void setAfter_bull(int after_bull) {
            this.after_bull = after_bull;
        }

        public int getFuel_max() {
            return fuel_max;
        }

        public void setFuel_max(int fuel_max) {
            this.fuel_max = fuel_max;
        }

        public int getBull_max() {
            return bull_max;
        }

        public void setBull_max(int bull_max) {
            this.bull_max = bull_max;
        }

        public int getBuild_time() {
            return build_time;
        }

        public void setBuild_time(int build_time) {
            this.build_time = build_time;
        }

        public List<Integer> getTaik() {
            return taik;
        }

        public void setTaik(List<Integer> taik) {
            this.taik = taik;
        }

        public List<Integer> getSouk() {
            return souk;
        }

        public void setSouk(List<Integer> souk) {
            this.souk = souk;
        }

        public List<Integer> getHoug() {
            return houg;
        }

        public void setHoug(List<Integer> houg) {
            this.houg = houg;
        }

        public List<Integer> getRaig() {
            return raig;
        }

        public void setRaig(List<Integer> raig) {
            this.raig = raig;
        }

        public List<Integer> getTyku() {
            return tyku;
        }

        public void setTyku(List<Integer> tyku) {
            this.tyku = tyku;
        }

        public List<Integer> getLuck() {
            return luck;
        }

        public void setLuck(List<Integer> luck) {
            this.luck = luck;
        }

        public List<Integer> getMax_eq() {
            return max_eq;
        }

        public void setMax_eq(List<Integer> max_eq) {
            this.max_eq = max_eq;
        }

        public List<Integer> getBroken() {
            return broken;
        }

        public void setBroken(List<Integer> broken) {
            this.broken = broken;
        }

        public List<Integer> getPow_up() {
            return pow_up;
        }

        public void setPow_up(List<Integer> pow_up) {
            this.pow_up = pow_up;
        }
    }

    public static class GraphEntity {
        private List<Integer> boko_n;
        private List<Integer> boko_d;
        private List<Integer> kaisyu_n;
        private List<Integer> kaisyu_d;
        private List<Integer> kaizo_n;
        private List<Integer> kaizo_d;
        private List<Integer> map_n;
        private List<Integer> map_d;
        private List<Integer> ensyuf_n;
        private List<Integer> ensyuf_d;
        private List<Integer> ensyue_n;
        private List<Integer> battle_n;
        private List<Integer> battle_d;
        private List<Integer> wed_a;
        private List<Integer> wed_b;

        public List<Integer> getBoko_n() {
            return boko_n;
        }

        public void setBoko_n(List<Integer> boko_n) {
            this.boko_n = boko_n;
        }

        public List<Integer> getBoko_d() {
            return boko_d;
        }

        public void setBoko_d(List<Integer> boko_d) {
            this.boko_d = boko_d;
        }

        public List<Integer> getKaisyu_n() {
            return kaisyu_n;
        }

        public void setKaisyu_n(List<Integer> kaisyu_n) {
            this.kaisyu_n = kaisyu_n;
        }

        public List<Integer> getKaisyu_d() {
            return kaisyu_d;
        }

        public void setKaisyu_d(List<Integer> kaisyu_d) {
            this.kaisyu_d = kaisyu_d;
        }

        public List<Integer> getKaizo_n() {
            return kaizo_n;
        }

        public void setKaizo_n(List<Integer> kaizo_n) {
            this.kaizo_n = kaizo_n;
        }

        public List<Integer> getKaizo_d() {
            return kaizo_d;
        }

        public void setKaizo_d(List<Integer> kaizo_d) {
            this.kaizo_d = kaizo_d;
        }

        public List<Integer> getMap_n() {
            return map_n;
        }

        public void setMap_n(List<Integer> map_n) {
            this.map_n = map_n;
        }

        public List<Integer> getMap_d() {
            return map_d;
        }

        public void setMap_d(List<Integer> map_d) {
            this.map_d = map_d;
        }

        public List<Integer> getEnsyuf_n() {
            return ensyuf_n;
        }

        public void setEnsyuf_n(List<Integer> ensyuf_n) {
            this.ensyuf_n = ensyuf_n;
        }

        public List<Integer> getEnsyuf_d() {
            return ensyuf_d;
        }

        public void setEnsyuf_d(List<Integer> ensyuf_d) {
            this.ensyuf_d = ensyuf_d;
        }

        public List<Integer> getEnsyue_n() {
            return ensyue_n;
        }

        public void setEnsyue_n(List<Integer> ensyue_n) {
            this.ensyue_n = ensyue_n;
        }

        public List<Integer> getBattle_n() {
            return battle_n;
        }

        public void setBattle_n(List<Integer> battle_n) {
            this.battle_n = battle_n;
        }

        public List<Integer> getBattle_d() {
            return battle_d;
        }

        public void setBattle_d(List<Integer> battle_d) {
            this.battle_d = battle_d;
        }

        public List<Integer> getWed_a() {
            return wed_a;
        }

        public void setWed_a(List<Integer> wed_a) {
            this.wed_a = wed_a;
        }

        public List<Integer> getWed_b() {
            return wed_b;
        }

        public void setWed_b(List<Integer> wed_b) {
            this.wed_b = wed_b;
        }
    }
}
