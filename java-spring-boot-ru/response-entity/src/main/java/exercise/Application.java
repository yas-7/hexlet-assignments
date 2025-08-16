package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;
import lombok.Setter;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    @Setter
    private static  List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
//    GET /posts — список всех постов. Должен возвращаться статус 200 и заголовок X-Total-Count, в котором содержится количество постов
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> index() {
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(posts.size())).body(posts);
    }
//    GET /posts/{id} – просмотр конкретного поста. Если пост найден, должен возвращаться статус 200, если нет — статус 404
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> show(@PathVariable String id) {
       return ResponseEntity.of(posts.stream().filter(p -> p.getId().equals(id)).findFirst());
    }
//    POST /posts – создание нового поста. Должен возвращаться статус 201
    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post post) {
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
//    PUT /posts/{id} – Обновление поста. Должен возвращаться статус 200. Если пост не существует, то должен возвращаться 404
    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        Optional<Post> maybePost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setBody(data.getBody());
        }
        return ResponseEntity.of(maybePost);
    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
