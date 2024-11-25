package com.utils;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * 返回结果类
 * <p>该类已重构为生成器模式实现</p>
 *
 * @param <T> the type parameter
 */
@NoArgsConstructor
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;


    /**
     * 私有构造器，供Builder内部类使用
     * @param builder Builder
     */
    private Result(@NotNull Builder<T> builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
    }


    /**
     *  两个方法用于构造有data和null data的ok
     * @return result
     * @param <T> Class Type
     */
    public static <T> @NotNull Result<T> ok() {
        return new Builder<T>()
                .data(null)
                .resultCodeEnum(ResultCodeEnum.SUCCESS)
                .build();
    }

    public static <T> @NotNull Result<T> ok(T data) {
        return new Builder<T>()
                .data(data)
                .resultCodeEnum(ResultCodeEnum.SUCCESS)
                .build();
    }

    /**
     * 静态内部类，用于构造Result类参数
     * @param <T> Class Type
     */
    public static class Builder<T> {
        private int code;
        private String msg;
        private T data = null;

        /**
         * 调用Result类的私有构造器，返回builder对象自己。
         * 此时builder对象已完成必要的参数赋值，将builder对象传过去，再在result构造方法中完成result类成员变量的赋值即可
         * @return result Result
         */
        public Result<T> build() {
            return new Result<>(this);
        }


        /**
         * 对参数赋值
         * @param code int
         * @return this
         */
        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }
        /**
         * 对参数赋值
         * @param msg String
         * @return this
         */
        public Builder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }
        /**
         * 对参数赋值
         * @param data T
         * @return this
         */
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }
        /**
         * 对参数赋值
         * @param resultCodeEnum ResultCodeEnum
         * @return this
         */
        public Builder<T> resultCodeEnum(@NotNull ResultCodeEnum resultCodeEnum) {
            this.code = resultCodeEnum.getCode();
            this.msg = resultCodeEnum.getMessage();
            return this;
        }
    }
}
