package wiki.feh.apitest.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamDto {
    private final String id;
    private final String name;
    private final int index;

    public TeamDto(String idName, int index) {
        String[] idNameArr = idName.split("#");
        this.id = idNameArr[0];
        this.name = idNameArr[1];
        this.index = index;
    }
}
