package com.team.todaycheck.main.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -730656103L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final ListPath<Comment, QComment> comment = this.<Comment, QComment>createList("comment", Comment.class, QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> postKey = createNumber("postKey", Integer.class);

    public final ListPath<Recommander, QRecommander> recommander = this.<Recommander, QRecommander>createList("recommander", Recommander.class, QRecommander.class, PathInits.DIRECT2);

    public final NumberPath<Integer> recommendation = createNumber("recommendation", Integer.class);

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    public final QUserEntity userEntity;

    public final StringPath userId = createString("userId");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userEntity = inits.isInitialized("userEntity") ? new QUserEntity(forProperty("userEntity")) : null;
    }

}

