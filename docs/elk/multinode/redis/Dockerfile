FROM redis:3.2

ENV REDIS_PASSWORD 123456
CMD ["sh", "-c", "exec redis-server --requirepass \"$REDIS_PASSWORD\""]