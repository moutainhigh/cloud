FROM redis

ENV REDIS_PASSWORD 123456

CMD ["sh", "-c", "exec redis-server --requirepass \"$REDIS_PASSWORD\""]
