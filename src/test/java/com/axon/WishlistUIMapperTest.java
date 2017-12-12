//package com.axon;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import com.axon.common.WishlistViewBean;
//import com.axon.query.UImapper.WishlistUIMapper;
//import com.axon.query.entity.Wishlist;
//
//@RunWith(MockitoJUnitRunner.class)
//public class WishlistUIMapperTest {
//
//	@InjectMocks
//	private WishlistUIMapper classUnderTest = new WishlistUIMapper();
//	
//	
//	@Test
//	public void createUIViewBean_WishlistsFound_ReturnViewBean(){
//		classUnderTest.createUIViewBeanList(wishlists());
//		WishlistViewBean viewBean = new WishlistViewBean();
//		
//		for(Wishlist wishlist : wishlists()){
//			viewBean.setName(wishlist.getName());
//			viewBean.setDescription(wishlist.getDescription());
//		}
//		
//		assertNotNull(viewBean);
//		assertEquals("wishlist a", viewBean.getName() );
//		assertEquals("description a", viewBean.getDescription());
//	}
//	
//	@Test
//	public void createUIViewBean_WishlistFound_ReturnViewBean(){
//		WishlistViewBean wishlist = classUnderTest.createUIViewBean(wishlist());
//		assertNotNull(wishlist);
//		assertEquals("wishlist a", wishlist.getName());
//	}
//
//	private Wishlist wishlist() {
//		Wishlist wishlist = new Wishlist();
//		wishlist.setName("wishlist a");
//		wishlist.setDescription("description a");
//		return wishlist;
//	}
//
//	private List<Wishlist> wishlists() {
//		List<Wishlist> list = new ArrayList<>();
//		Wishlist wishlist = new Wishlist();
//		wishlist.setName("wishlist a");
//		wishlist.setDescription("description a");
//		list.add(wishlist);
//		return list;
//	}
//}
//
