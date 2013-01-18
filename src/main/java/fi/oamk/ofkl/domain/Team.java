package fi.oamk.ofkl.domain;

public class Team {

    private final String name;
    private final Level level;

    public Team(String name, Level level) {
        this.name = name;
        this.level = level;
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
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }
}
