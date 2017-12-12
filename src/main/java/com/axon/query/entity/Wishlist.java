package com.axon.query.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.axon.common.Privacy;
import com.axon.common.Source;
import com.axon.common.Type;
import com.axon.write.aggregates.WishlistId;

@Entity
@Table(name = "wishlist")
public class Wishlist implements Serializable{

	private static final long serialVersionUID = -2517851941873251697L;
	
	@Id
	private String wishlistId;

	private String name;

	private String description;

	private String client;

	private String locale;

	private Source source;

	private Type type;

	private Privacy privacy;

	private String userId;

	private Date createdAt;

	private Date modifiedOn;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "WISHLIST_ITEM", joinColumns = @JoinColumn(name = "WISHLIST_ID"), inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
	private List<Item> items;

	public Wishlist() {
	}
	
	public Wishlist(WishlistId wishlistId, String name, String description, String client, String locale,
			Date createdAt, Source source, Type type, Privacy privacy, String userId, Date modifiedOn,
			List<Item> items) {
		System.out.println("Instatiate Wishlist ...");
		this.wishlistId = wishlistId.getIdentifier();
		this.name = name;
		this.description = description;
		this.client = client;
		this.locale = locale;
		this.createdAt = createdAt;
		this.source = source;
		this.type = type;
		this.privacy = privacy;
		this.userId = userId;
		this.modifiedOn = modifiedOn;
		this.items = items;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setPrivacy(Privacy privacy) {
		this.privacy = privacy;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getWishlistId() {
		return wishlistId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getClient() {
		return client;
	}

	public String getLocale() {
		return locale;
	}

	public Source getSource() {
		return source;
	}

	public Type getType() {
		return type;
	}

	public Privacy getPrivacy() {
		return privacy;
	}

	public String getUserId() {
		return userId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public List<Item> getItems() {
		return items;
	}

	public void addWishlistItem(Item item) {
			handleAdd(items, item);
	}

	private void handleAdd(List<Item> items, Item item) {
		if(items.size()==0){
			items.add(item);
			setItems(items);
		}else{
			System.out.println("items: " + items);
			for (Item itemValue : items) {
				if (itemValue.getItemId().equals(item.getItemId())) {
					break;
				} else {
					items.add(item);
					setItems(items);
					break;
				}
			}
		}
		System.out.println("items: " + items);
	}

	public void deleteWishlistItem(Item item) {
		handleDelete(items, item);
	}

	private void handleDelete(List<Item> items, Item item) {

		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item itemValue = iterator.next();
			
			System.out.println("item value: " + itemValue);
			if (item.getItemId().equals(itemValue.getItemId())) {
				iterator.remove();
				setItems(items);
			}
		}
		
		System.out.println("item list after delete: " + items);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((wishlistId == null) ? 0 : wishlistId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wishlist other = (Wishlist) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (wishlistId == null) {
			if (other.wishlistId != null)
				return false;
		} else if (!wishlistId.equals(other.wishlistId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Wishlist [wishlistId=" + wishlistId + ", name=" + name + ", description=" + description + ", client="
				+ client + ", locale=" + locale + ", source=" + source + ", type=" + type + ", privacy=" + privacy
				+ ", userId=" + userId + ", createdAt=" + createdAt + ", modifiedOn=" + modifiedOn + ", items=" + items
				+ "]";
	}

}