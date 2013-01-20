package fi.oamk.ofkl.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRange;
import org.drools.planner.api.domain.variable.ValueRangeType;

@PlanningEntity(difficultyComparatorClass = TeamAssignmentDifficultyComparator.class)
public class TeamAssignment {

    private Player player;

    public TeamAssignment(Player player) {
        this.player = player;
    }

    @PlanningVariable
    @ValueRange(type = ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty = "playerList")
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * The normal methods {@link #equals(Object)} and {@link #hashCode()} cannot be used because the rule engine already
     * requires them (for performance in their original state).
     * @see #solutionHashCode()
     */
    public boolean solutionEquals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof TeamAssignment) {
            TeamAssignment other = (TeamAssignment) o;
            return new EqualsBuilder()
                    .append(player, other.player)
                    .isEquals();
        } else {
            return false;
        }
    }

    public TeamAssignment clone() {
        TeamAssignment clone = new TeamAssignment(player);
        return clone;
    }


    /**
     * The normal methods {@link #equals(Object)} and {@link #hashCode()} cannot be used because the rule engine already
     * requires them (for performance in their original state).
     * @see #solutionEquals(Object)
     */
    public int solutionHashCode() {
        return new HashCodeBuilder()
                .append(player)
                .toHashCode();
    }

    @Override
    public String toString() {
        return player.toString();
    }


}
