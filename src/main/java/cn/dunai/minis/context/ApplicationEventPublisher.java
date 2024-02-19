package cn.dunai.minis.context;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
