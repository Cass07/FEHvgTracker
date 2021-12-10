package wiki.feh.apitest.domain.vginfo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static wiki.feh.apitest.domain.vginfo.QVgInfo.vgInfo;

@RequiredArgsConstructor
@Repository
public class VgInfoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public VgInfo getLatestVgInfo()
    {
        return queryFactory
                .selectFrom(vgInfo)
                .orderBy(vgInfo.vgNumber.desc())
                .fetchFirst();
    }

    public List<VgInfo> findAllDecs()
    {
        return queryFactory
                .selectFrom(vgInfo)
                .orderBy(vgInfo.id.desc())
                .fetch();
    }

    public VgInfo findByVgnumber(int vgNumber)
    {
        return queryFactory
                .selectFrom(vgInfo)
                .where(vgInfo.vgNumber.eq(vgNumber))
                .fetchFirst();
    }

}
