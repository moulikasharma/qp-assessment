package test.grocerybooking.grocerybooking.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import test.grocerybooking.grocerybooking.entities.CheckoutItem;
import test.grocerybooking.grocerybooking.entities.GroceryItem;
import test.grocerybooking.grocerybooking.entities.Users;
import test.grocerybooking.grocerybooking.repository.CheckoutItemRepository;
import test.grocerybooking.grocerybooking.repository.GroceryRepository;
import test.grocerybooking.grocerybooking.repository.UsersRepository;

@Controller
public class GroceryController {
	
	public GroceryRepository groceryRepository;
	public UsersRepository usersRepository;
	public CheckoutItemRepository checkoutItemRepository;
	
	public GroceryController(GroceryRepository groceryRepository, UsersRepository usersRepository,
			CheckoutItemRepository checkoutItemRepository) {
		this.groceryRepository = groceryRepository;
		this.usersRepository = usersRepository;
		this.checkoutItemRepository = checkoutItemRepository;
	}
	
	@RequestMapping("/")
    public String rootPage() {
        return "redirect:/home";
    }
    
    @RequestMapping("/home")
    public String home(ModelMap model){
        model.addAttribute("title", "Welcome");
        return "home";
    }
    
    //Login page for admin GET (shows login form)
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){
    	model.addAttribute("pageTitle", "Login");
        model.addAttribute("givenAction", "/login");
        return "login";
    }
    
    //Login page for admin POST (on successsful login- redirect to inventory page)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String uname, @RequestParam String psw){
    	Optional<Users> user = usersRepository.getByUsername(uname);
    	if(user.isPresent()) {
    		if(!user.get().getPassword().equals(psw) || !user.get().getIsAdmin()) {
    			return "incorrectUser";
    		}
    	}else {
    		return "noUser";
    	}
    	return "redirect:/groceryList";
    }
    
    //admin - inventory page
    @RequestMapping(value = "/groceryList", method = RequestMethod.GET)
    public String groceryList(Model model){
        List<GroceryItem> allItems = groceryRepository.findAll();
        model.addAttribute("items", allItems);
        return "inventory";
    }
    
    //admin - add item GET
    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public String newItem(Model model){
    	model.addAttribute("pageTitle", "New Item");
        model.addAttribute("givenAction", "/addItem");
        return "addItem";
    }
    
  //admin - add item POST (on successsful entry - redirect to inventory page)
    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public String addItem(@RequestParam String name, @RequestParam BigDecimal price, @RequestParam Long quantity){
    	GroceryItem newItem = new GroceryItem();
    	newItem.setName(name);
    	newItem.setPrice(price);
    	newItem.setQuantity(quantity);
    	groceryRepository.save(newItem);
    	return "redirect:/groceryList";
    }
    
    //admin - edit item in inventory GET
    @RequestMapping(value = "/editItem", method = RequestMethod.GET)
    public String editItem(@RequestParam(value = "id") Long groceryId, Model model) {
    	model.addAttribute("pageTitle", "Edit Item");
        model.addAttribute("givenAction", "/editItem");
        
        Optional<GroceryItem> itemOptional = groceryRepository.findById(groceryId);
        if (itemOptional.isPresent()) {
            GroceryItem item = itemOptional.get();
            model.addAttribute("item", item);
            groceryRepository.deleteById(item.getId());
            return "editItem"; 
        } else {
            // Handle case where item with given ID is not found
            return "redirect:/groceryList"; 
        } 
    }


    //admin - edit item in inventory POST
    @RequestMapping(value = "/editItem", method = RequestMethod.POST)
    public String editItem(@ModelAttribute("item") GroceryItem item) {
    	groceryRepository.save(item); 
        return "redirect:/groceryList"; 
    }

    //admin - delete item
    @RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
    public String deleteItem(@RequestParam("id") Long itemId) {
        groceryRepository.deleteById(itemId);
        return "redirect:/groceryList"; 
    }
    
    //user- show inventory to user
    @RequestMapping(value = "/viewItems", method = RequestMethod.GET)
    public String groceryListForUser(Model model){
        List<GroceryItem> allItems = groceryRepository.findAll();
        
        
        for(GroceryItem item : allItems) {
        	if(item.getQuantity() > 0L) {
        		Optional<CheckoutItem> gItem =  checkoutItemRepository.getByName(item.getName());
        		if(gItem.isEmpty()) {
        			CheckoutItem chkOutItem = new CheckoutItem();
                    chkOutItem.setName(item.getName());
                    chkOutItem.setPrice(item.getPrice());
                    Long qty = 0L;
                    chkOutItem.setQuantity(qty); //Change
                    BigDecimal amt = new BigDecimal(qty).multiply(item.getPrice());
                    chkOutItem.setAmount(amt);
                    checkoutItemRepository.save(chkOutItem);
        		}
        	}
        }
        List<CheckoutItem> allChkOutItems = checkoutItemRepository.findAll();
        model.addAttribute("items", allChkOutItems);
        
        return "viewItems";
    }
    
    //user - add item to be check out out GET
    @RequestMapping(value = "/addCheckoutItem", method = RequestMethod.GET)
    public String addCheckoutItem(@RequestParam(value = "id") Long id, Model model) {
    	model.addAttribute("pageTitle", "Add Item To Checkout");
        model.addAttribute("givenAction", "/addCheckoutItem");
        System.out.println(id);
        Optional<CheckoutItem> itemOptional = checkoutItemRepository.findById(id);
        if (itemOptional.isPresent()) {
        	CheckoutItem item = itemOptional.get();
        	System.out.println(item);
            model.addAttribute("item", item); 
        }
        
        return "addCheckoutItem";
    }


    //admin - edit item in inventory POST
    @RequestMapping(value = "/addCheckoutItem", method = RequestMethod.POST)
    public String saveCheckoutItem(@ModelAttribute("item") CheckoutItem item) {
    	
    	BigDecimal amt = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
    	item.setAmount(amt);
    	checkoutItemRepository.deleteById(item.getId());
    	checkoutItemRepository.save(item);
        return "redirect:/viewItems"; 
    }

    @RequestMapping(value = "/checkoutPage", method = RequestMethod.GET)
    public void checkoutPage(Model model){
    	List<CheckoutItem> allChkOutItems = checkoutItemRepository.findAll();
    	Long totalQty = 0L;
    	BigDecimal totalAmt = BigDecimal.ZERO;
    	for(CheckoutItem item : allChkOutItems) {
    		totalQty += item.getQuantity();
    		totalAmt = totalAmt.add(item.getAmount());
    	}
    	model.addAttribute("totalQty", totalQty);
    	model.addAttribute("totalAmt", totalAmt);
    	model.addAttribute("items", allChkOutItems);
    }
    
    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thanks(Model model){
        return "thanks";
    }

}
