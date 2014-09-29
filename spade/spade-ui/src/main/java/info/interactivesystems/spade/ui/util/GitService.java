package info.interactivesystems.spade.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/git")
public class GitService {

	@Resource
	GitRepositoryStateFactory factory;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public
	@ResponseBody
	GitRepositoryState checkGitRevision() {
		try {
			return factory.getGitRepositoryState();
		} catch (IOException e) {
			log.error("Error Loading git properties");
			return null;
		}
	}
}