package com.example.fx;

import java.util.Optional;

final class Either<L,R>
{
    public static <L,R> Either<L,R> left(L value) {
        return new Either<>(Optional.of(value), Optional.empty());
    }
    public static <L,R> Either<L,R> right(R value) {
        return new Either<>(Optional.empty(), Optional.of(value));
    }
    private final Optional<L> left;
    private final Optional<R> right;
    private Either(Optional<L> l, Optional<R> r) {
        left=l;
        right=r;
    }
    public boolean isLeft(){
        return left.isPresent();
    }
    public boolean isRight(){
        return right.isPresent();
    }
    public R right(){
        return right.get();
    }
    public L left() {
        return left.get();
    }
}