package com.project.forum.service.implement;

import com.project.forum.service.ICacheService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CacheService implements ICacheService {

    // Sử dụng ConcurrentHashMap thay thế Redis
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void saveData(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getData(String key) {
        return cache.get(key);
    }

    @Override
    public void deleteData(String key) {
        cache.remove(key);
    }

    @Override
    public void saveDataWithTime(String key, Object value, Long time) {
        cache.put(key, value);

        // Tạo một thread mới để xóa cache sau khoảng thời gian nhất định
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(time);
                cache.remove(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean existData(String key) {
        return cache.containsKey(key);
    }

    @Override
    public void saveDateNoValue(String key, Long time) {
        // Lưu trữ giá trị rỗng và xóa sau khoảng thời gian
        cache.put(key, "");
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(time);
                cache.remove(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
