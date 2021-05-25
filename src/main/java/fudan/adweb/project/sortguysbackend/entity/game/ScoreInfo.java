package fudan.adweb.project.sortguysbackend.entity.game;

public class ScoreInfo {
    private String username;
    private Double score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public ScoreInfo(){
        // empty
    }

    public ScoreInfo(String username, Double score) {
        this.username = username;
        this.score = score;
    }
}
