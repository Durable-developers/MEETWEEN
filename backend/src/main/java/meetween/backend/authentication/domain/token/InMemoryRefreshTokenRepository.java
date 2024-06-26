package meetween.backend.authentication.domain.token;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import meetween.backend.authentication.exception.NoSuchRefreshTokenException;
import org.springframework.stereotype.Component;

@Component
public class InMemoryRefreshTokenRepository implements RefreshTokenRepository {
    private static final Map<Long, String> storage = new ConcurrentHashMap<>();

    @Override
    public void save(final long memberId, final String refreshToken) {
        storage.put(memberId, refreshToken);
    }

    @Override
    public String findById(long memberId) {
        Optional<String> refreshToken = Optional.ofNullable(storage.get(memberId));
        return refreshToken.orElseThrow(() -> new NoSuchRefreshTokenException());
    }

    @Override
    public boolean existsById(long memberId) {
        return storage.containsKey(memberId);
    }

    @Override
    public long deleteById(long memberId) {
        if(!existsById(memberId)) {
            throw new NoSuchRefreshTokenException();
        }
        storage.remove(memberId);
        return memberId;
    }
}
