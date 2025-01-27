package wiki.feh.apitest.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VgViewDto {

    private VgInfoGetDto vgInfoEntity;
    private List<VgDataGetDto> currentRoundVgdata;
    private List<VgDataResultGetDto> round1Vgdata;
    private List<VgDataResultGetDto> round2Vgdata;
    private List<VgDataResultGetDto> round3Vgdata;

    private Map<String, String> viewModel;//view로 넘겨줄 string들 attributeName이랑 Value 담고잇는거 (errorMessage도 여기에 담으셈)
    private String viewString;

    private List<TeamDto> teamList;
}
