package cn.wandersnail.ble;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.wandersnail.ble.util.Logger;
import cn.wandersnail.commons.observer.Observable;
import cn.wandersnail.commons.observer.Observe;
import cn.wandersnail.commons.poster.ThreadMode;

/**
 * date: 2019/8/3 12:02
 * author: zengfansheng
 */
public class EasyBLEBuilder {
    private final static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    BondController bondController;
    DeviceCreator deviceCreator;
    ThreadMode methodDefaultThreadMode = ThreadMode.MAIN;
    ExecutorService executorService = DEFAULT_EXECUTOR_SERVICE;
    ScanConfiguration scanConfiguration;
    Observable observable;
    Logger logger;
    boolean isObserveAnnotationRequired = false;
    ScannerType scannerType;

    EasyBLEBuilder() {
    }

    /**
     * 指定蓝牙扫描器，默认为系统Android5.0以上使用{@link ScannerType#LE}，否则使用{@link ScannerType#LEGACY}。
     * 系统小于Android5.0时，指定{@link ScannerType#LE}无效
     */
    public EasyBLEBuilder setScannerType(@NonNull ScannerType scannerType) {
        this.scannerType = scannerType;
        return this;
    }

    /**
     * 自定义线程池用来执行后台任务
     */
    public EasyBLEBuilder setExecutorService(@NonNull ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    /**
     * 设备实例构建器
     */
    public EasyBLEBuilder setDeviceCreator(@NonNull DeviceCreator deviceCreator) {
        this.deviceCreator = deviceCreator;
        return this;
    }

    /**
     * 配对控制器。如果设置了控制器，则会在连接时，尝试配对
     */
    public EasyBLEBuilder setBondController(@NonNull BondController bondController) {
        this.bondController = bondController;
        return this;
    }

    /**
     * 观察者或者回调的方法在没有使用注解指定调用线程时，默认被调用的线程
     */
    public EasyBLEBuilder setMethodDefaultThreadMode(@NonNull ThreadMode mode) {
        methodDefaultThreadMode = mode;
        return this;
    }

    /**
     * 搜索配置
     */
    public EasyBLEBuilder setScanConfiguration(@NonNull ScanConfiguration scanConfiguration) {
        this.scanConfiguration = scanConfiguration;
        return this;
    }

    /**
     * 日志打印
     */
    public EasyBLEBuilder setLogger(@NonNull Logger logger) {
        this.logger = logger;
        return this;
    }

    /**
     * 被观察者，消息发布者。
     * <br>如果观察者被设置，{@link #setMethodDefaultThreadMode(ThreadMode)}、
     * {@link #setObserveAnnotationRequired(boolean)}、{@link #setExecutorService(ExecutorService)}将不起作用
     */
    public EasyBLEBuilder setObservable(@NonNull Observable observable) {
        this.observable = observable;
        return this;
    }

    /**
     * 是否强制使用{@link Observe}注解才会收到被观察者的消息
     * 
     * @param observeAnnotationRequired true：只有方法上加{@link Observe}注解的才会收到消息。false：加不加注解都会收到消息
     */
    public EasyBLEBuilder setObserveAnnotationRequired(boolean observeAnnotationRequired) {
        isObserveAnnotationRequired = observeAnnotationRequired;
        return this;
    }

    /**
     * 根据当前配置构建EasyBLE实例
     */
    public EasyBLE build() {
        synchronized (EasyBLE.class) {
            if (EasyBLE.instance != null) {
                throw new EasyBLEException("EasyBLE instance already exists. It can only be instantiated once.");
            }
            EasyBLE.instance = new EasyBLE(this);
            return EasyBLE.instance;
        }
    }
}
