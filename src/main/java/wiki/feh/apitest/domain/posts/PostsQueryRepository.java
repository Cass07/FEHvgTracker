package wiki.feh.apitest.domain.posts;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wiki.feh.apitest.dto.PostsGetWithPicDto;

import java.util.List;

import static wiki.feh.apitest.domain.posts.QPosts.posts;
import static wiki.feh.apitest.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class PostsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Posts> findAllDecs(){
        return queryFactory
                .selectFrom(posts)
                .orderBy(posts.id.desc())
                .fetch();
    }

    public List<Posts> findByTitle(String title){
        return queryFactory
                .selectFrom(posts)
                .where(posts.title.eq(title))
                .fetch();
    }

    public List<PostsGetWithPicDto> getPostsWithPicture(long id)
    {
        return queryFactory
                .select(Projections.constructor(PostsGetWithPicDto.class,
                        posts.id, posts.title, posts.content, posts.author, user.picture, user.name, posts.createDate, posts.modifiedDate))
                .from(posts)
                .innerJoin(user).on(posts.author.eq(user.email))
                .where(posts.id.eq(id))
                .fetch();
    }
}
