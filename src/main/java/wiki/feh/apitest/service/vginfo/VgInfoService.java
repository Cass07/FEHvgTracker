package wiki.feh.apitest.service.vginfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.domain.vginfo.VgInfoQueryRepository;
import wiki.feh.apitest.domain.vginfo.VgInfoRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VgInfoService {
    private final VgInfoRepository vgInfoRepository;
    private final VgInfoQueryRepository vgInfoQueryRepository;

    @Transactional(readOnly = true)
    public Optional<VgInfo> findById(long id) {
        return vgInfoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<VgInfo> getLatestVgInfo() {
        return vgInfoQueryRepository.getLatestVgInfo();
    }

    @Transactional(readOnly = true)
    public List<VgInfo> findAllDesc() {
        return vgInfoQueryRepository.findAllDesc();
    }

    @Transactional
    public VgInfo save(VgInfo entity) {
        return vgInfoRepository.save(entity);
    }

}
