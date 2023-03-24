/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import commons.Board;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import server.api.BoardController;
import server.database.BoardRepository;

import java.util.ArrayList;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class Main {

    private static long DEFAULT_ID;

    /**
     * start the server
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * Checks if the default board is in the database
     * If not, create a new one
     * @param repo the Board repository
     * @return
     */
    @Bean
    public CommandLineRunner defaultBoardCheck(
            BoardController ctrl,
            BoardRepository repo
    ) {
        return (args) -> {
            DEFAULT_ID = ctrl.getDefaultId();
            if (!repo.findById(DEFAULT_ID).isPresent()) {
                Board defaultBoard = new Board(
                        "DEFAULT", "Default Board", new ArrayList<>());
                long oldId = repo.save(defaultBoard).getId();
                repo.updateBoardId(oldId, DEFAULT_ID);
                System.out.println("Default Board Created");
            }
            else {
                System.out.println("Default Board already exists");
            }
        };
    }
}