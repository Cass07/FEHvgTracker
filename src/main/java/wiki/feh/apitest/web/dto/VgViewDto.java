package wiki.feh.apitest.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class VgViewDto {

    private VgInfoGetDto vgInfoEntity;
    private List<VgDataGetDto> currentRoundVgdata;
    private List<VgDataResultGetDto> round1Vgdata;
    private List<VgDataResultGetDto> round2Vgdata;
    private List<VgDataResultGetDto> round3Vgdata;

    private Map<String, String> viewModel;//view로 넘겨줄 string들 attributeName이랑 Value 담고잇는거 (errorMessage도 여기에 담으셈)
    private String viewString;

    @Builder
    public VgViewDto(VgInfoGetDto vgInfoEntity, List<VgDataGetDto> currentRoundVgdata,
                     List<VgDataResultGetDto> round1Vgdata,List<VgDataResultGetDto> round2Vgdata,List<VgDataResultGetDto> round3Vgdata,
                     String viewString, Map<String, String> viewModel)
    {
        this.currentRoundVgdata = currentRoundVgdata;
        this.round1Vgdata = round1Vgdata;
        this.round2Vgdata = round2Vgdata;
        this.round3Vgdata = round3Vgdata;
        this.vgInfoEntity = vgInfoEntity;
        this.viewModel = viewModel;
        this.viewString = viewString;
    }

}
