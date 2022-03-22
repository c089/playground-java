package infrastructure;

import domain.GameWonBy;
import domain.Player;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table( name = "GameWonBy" )
public class GameWonByEntity {
    @Enumerated
    @Column(name = "player")
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private Long id;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    GameWonByEntity() {
    }

    public GameWonByEntity(GameWonBy gameWonBy) {
        this.setPlayer(gameWonBy.player());
    }

}
