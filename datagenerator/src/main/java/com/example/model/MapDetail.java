package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/4/23.
 */
public class MapDetail {
    public MapDetail() {
        setName(new MultiLanguageEntry());
        setPoints(new ArrayList<>());
        setDrop(new ArrayList<>());
        setRoute(new ArrayList<>());
    }

    private MultiLanguageEntry name;

    private List<PointsEntity> points;
    /**
     * name : A
     * ship : [51,51,51]
     */

    private List<DropEntity> drop;
    /**
     * start : A
     * target : ["A","A"]
     * condition :
     */

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List<RouteEntity> route;

    public MultiLanguageEntry getName() {
        return name;
    }

    public void setName(MultiLanguageEntry name) {
        this.name = name;
    }

    public List<PointsEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointsEntity> points) {
        this.points = points;
    }

    public List<DropEntity> getDrop() {
        return drop;
    }

    public void setDrop(List<DropEntity> drop) {
        this.drop = drop;
    }

    public List<RouteEntity> getRoute() {
        return route;
    }

    public void setRoute(List<RouteEntity> route) {
        this.route = route;
    }

    public static class PointsEntity {

        private String point;


        private MultiLanguageEntry name;
        private int type;
        /**
         * ships : [502,501,-1,-1,-1,-1]
         * formation : 1
         */

        private List<FleetsEntity> fleets;

        private ResourceEntity resource;

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public MultiLanguageEntry getName() {
            return name;
        }

        public void setName(MultiLanguageEntry name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<FleetsEntity> getFleets() {
            return fleets;
        }

        public void setFleets(List<FleetsEntity> fleets) {
            this.fleets = fleets;
        }

        public ResourceEntity getResource() {
            return resource;
        }

        public void setResource(ResourceEntity resource) {
            this.resource = resource;
        }

        public static class FleetsEntity {
            private int formation;
            private List<Integer> ships;

            public FleetsEntity() {
                setShips(new ArrayList<>());
            }

            public int getFormation() {
                return formation;
            }

            public void setFormation(int formation) {
                this.formation = formation;
            }

            public List<Integer> getShips() {
                return ships;
            }

            public void setShips(List<Integer> ships) {
                this.ships = ships;
            }
        }

        public static class ResourceEntity {
            private String type;
            private String count;

            public void setType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getCount() {
                return count;
            }
        }
    }

    public static class DropEntity {
        private String name;
        private List<Integer> ship;

        public DropEntity() {
            setShip(new ArrayList<>());
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Integer> getShip() {
            return ship;
        }

        public void setShip(List<Integer> ship) {
            this.ship = ship;
        }
    }

    public static class RouteEntity {
        private String start;
        private String condition;
        private List<String> target;

        public RouteEntity() {
            setTarget(new ArrayList<>());
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public List<String> getTarget() {
            return target;
        }

        public void setTarget(List<String> target) {
            this.target = target;
        }
    }

}
