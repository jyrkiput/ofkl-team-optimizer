package fi.oamk.ofkl.domain;

public class Team {

    private final String name;
    private final Level level;
    private int hashCode;

    public Team(String name, Level level) {
        this.name = name;
        this.level = level;
        hashCode = name != null ? name.hashCode() : 0;
        hashCode = 31 * hashCode + (level != null ? level.hashCode() : 0);
    }

    public enum Level {
        A, B
    }

    public Level getLevel() {
        return level;
    }

    public String toString() {
        return name + " " + level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (level != team.level) return false;
        if (name != null ? !name.equals(team.name) : team.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
