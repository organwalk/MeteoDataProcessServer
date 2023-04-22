package com.weather.utils;



public class Result {
    private int success;
    private int id;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Result() {

    }
    public Result(int success) {
        this.success = success;
    }

    public static Result success(){
        Result result = new Result();
        result.setSuccess(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(int id){
        Result result = new Result();
        result.setSuccess(ResultCode.SUCCESS);
        result.setId(id);
        return result;
    }

    public static Result fail(){
        Result result = new Result(ResultCode.FAIL);
        return result;
    }
}
