package com.sofi.apinfcbluetooth.domain.device.result;

public abstract class AppResult<T> {

    private AppResult() {
    }

    public static final class Success<T> extends AppResult<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public static final class Failure<T> extends AppResult<T> {
        private final AppError error;

        public Failure(AppError error) {
            this.error = error;
        }

        public AppError getError() {
            return error;
        }
    }
}
