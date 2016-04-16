package br.com.rrodovalho.taskerbot.rest;

import br.com.rrodovalho.taskerbot.bot.TaskerBot;
import br.com.rrodovalho.taskerbot.domain.Tasker;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;



/**
 * Created by rrodovalho on 12/04/16.
 */

@RestController
public class APIController {


    @RequestMapping(value = "/tasker",method = RequestMethod.POST)
    public @ResponseBody
    String allocTasker(@RequestBody String content){

        Tasker tasker = new Gson().fromJson(content, Tasker.class);

        TaskerBot bot = new TaskerBot(tasker);
        String json = new Gson().toJson(bot.alloc());
        System.out.println(json);
        return json;
    }

}
