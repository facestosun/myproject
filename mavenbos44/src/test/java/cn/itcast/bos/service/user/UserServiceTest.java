package cn.itcast.bos.service.user;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class UserServiceTest {
	//注入要测试的bean
	@Autowired
	private UserService userService;

	@Test
	public void testSaveUser() {
		User user = new User();
//		user.setId("8a7e854f5a9761e5015a9761f9720000");
		user.setUsername("admin");
		user.setPassword("admin");
		userService.saveUser(user);
	}

	@Test
	public void testFindUserList() {
		List<User> userList = userService.findUserList();
		System.out.println(userList);
	}
	@Test
	public void testFindByUsername() {
		User user = userService.findByUsername("admin");
		System.out.println(user);
	}
	@Test
	public void testFindByUsernameLike() {
		List<User> userList = userService.findByUsernameLike("ad");
		System.out.println(userList);
	}
	@Test
	public void testFindPasswordByUsername() {
		String password = userService.findPasswordByUsername("admin");
		System.out.println(password);
	}
	@Test
	public void testFindPasswordByUsername2() {
		String password = userService.findPasswordByUsername2("admin");
		System.out.println(password);
	}

}
