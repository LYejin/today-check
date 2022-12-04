package com.team.todaycheck.main.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecommander is a Querydsl query type for Recommander
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommander extends EntityPathBase<Recommander> {

    private static final long serialVersionUID = 1471712332L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecommander recommander = new QRecommander("recommander");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPost post;

    public final StringPath recommender = createString("recommender");

    public QRecommander(String variable) {
        this(Recommander.class, forVariable(variable), INITS);
    }

    public QRecommander(Path<? extends Recommander> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecommander(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecommander(PathMetadata metadata, PathInits inits) {
        this(Recommander.class, metadata, inits);
    }

    public QRecommander(Class<? extends Recommander> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

