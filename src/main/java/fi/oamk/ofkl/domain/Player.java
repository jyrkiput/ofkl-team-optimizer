package fi.oamk.ofkl.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {

    @JsonProperty("id")
    private int id;
    @JsonProperty("saludo")
    private int price;
    @JsonProperty("points")
    private double points;
    @JsonProperty("sarja")
    private char level;
    @JsonProperty("joukkue")
    private String teamName;
    @JsonProperty("nimi")
    private String name;
    private int hashCode;
    private Team team;

    public Player() {
        long temp;
        hashCode = id;
        hashCode = 31 * hashCode + price;
        temp = points != +0.0d ? Double.doubleToLongBits(points) : 0L;
        hashCode = 31 * hashCode + (int) (temp ^ (temp >>> 32));
        hashCode = 31 * hashCode + (int) level;
        hashCode = 31 * hashCode + (team != null ? team.hashCode() : 0);
        hashCode = 31 * hashCode + (name != null ? name.hashCode() : 0);
    }

    public Team getTeam() {
        if(team == null) {
            team = new Team(teamName, Team.Level.valueOf(Character.toString(level).toUpperCase()));
        }
        return team;
    }

    public int getPrice() {
        return price;
    }

    public double getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + " -> " + team + ", saludo " + price + ", points " + points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        if (level != player.level) return false;
        if (Double.compare(player.points, points) != 0) return false;
        if (price != player.price) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (team != null ? !team.equals(player.team) : player.team != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
