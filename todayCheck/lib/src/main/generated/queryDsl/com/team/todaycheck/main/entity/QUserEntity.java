package com.team.todaycheck.main.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -1168256313L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final EnumPath<UserEntity.Admin> admin = createEnum("admin", UserEntity.Admin.class);

    public final StringPath id = createString("id");

    public final ListPath<Mission, QMission> mission = this.<Mission, QMission>createList("mission", Mission.class, QMission.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<Post, QPost> post = this.<Post, QPost>createList("post", Post.class, QPost.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> roles = this.<String, StringPath>createList("roles", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

