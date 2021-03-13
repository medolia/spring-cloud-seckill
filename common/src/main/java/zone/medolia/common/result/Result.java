package zone.medolia.common.result;


public class Result<T> {
    private static final int SUCCESS_CODE = 0;

    private int code;
    private CodeMsg msg;
    private T data;

    public Result() {
    }

    public Result(int code, CodeMsg msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, CodeMsg msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    // 成功获得结果时
    public static <T> Result<T> success(T data) {
        return new Result<T>(SUCCESS_CODE, data);
    }

    // 出现错误时
    public static <T> Result<T> error(CodeMsg msg) {
        return new Result<T>(msg.getCode(), msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CodeMsg getMsg() {
        return msg;
    }

    public void setMsg(CodeMsg msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
