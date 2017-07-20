package com.meiyin.erp.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @param公告
 * @Time 2016-5-24
 */
@Entity
public class Topic_Entity {
	@Id
	private Long topic_code;
	private String istop;//优先级 1
	private String update_user;//修改人
	private String can_read;//能读
	private String ucode_name;
	private String state;
	private boolean isself;
	private String self;
	private String repeatcount;//0
	private String stateDesc;
	private String title;//标题
	private String unreadcount;//未读人数
	private String update_time;//修改时间
	private String topic_type;
	private Long create_time;//创建时间
	private String create_user;//创建人
	private String clickcount;//点击数
	private String readcount;//已读人数
	private boolean readflag;//是否查看
	private String content;//内容
	public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean getReadflag() {
		return this.readflag;
	}
	public void setReadflag(boolean readflag) {
		this.readflag = readflag;
	}
	public String getReadcount() {
		return this.readcount;
	}
	public void setReadcount(String readcount) {
		this.readcount = readcount;
	}
	public String getClickcount() {
		return this.clickcount;
	}
	public void setClickcount(String clickcount) {
		this.clickcount = clickcount;
	}
	public String getCreate_user() {
		return this.create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public Long getCreate_time() {
		return this.create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public String getTopic_type() {
		return this.topic_type;
	}
	public void setTopic_type(String topic_type) {
		this.topic_type = topic_type;
	}
	public String getUpdate_time() {
		return this.update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getUnreadcount() {
		return this.unreadcount;
	}
	public void setUnreadcount(String unreadcount) {
		this.unreadcount = unreadcount;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStateDesc() {
		return this.stateDesc;
	}
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
	public String getRepeatcount() {
		return this.repeatcount;
	}
	public void setRepeatcount(String repeatcount) {
		this.repeatcount = repeatcount;
	}
	public String getSelf() {
		return this.self;
	}
	public void setSelf(String self) {
		this.self = self;
	}
	public boolean getIsself() {
		return this.isself;
	}
	public void setIsself(boolean isself) {
		this.isself = isself;
	}
	public String getState() {
		return this.state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUcode_name() {
		return this.ucode_name;
	}
	public void setUcode_name(String ucode_name) {
		this.ucode_name = ucode_name;
	}
	public String getCan_read() {
		return this.can_read;
	}
	public void setCan_read(String can_read) {
		this.can_read = can_read;
	}
	public String getUpdate_user() {
		return this.update_user;
	}
	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}
	public String getIstop() {
		return this.istop;
	}
	public void setIstop(String istop) {
		this.istop = istop;
	}
	public Long getTopic_code() {
		return this.topic_code;
	}
	public void setTopic_code(Long topic_code) {
		this.topic_code = topic_code;
	}
	@Generated(hash = 943482725)
	public Topic_Entity(Long topic_code, String istop, String update_user,
			String can_read, String ucode_name, String state, boolean isself,
			String self, String repeatcount, String stateDesc, String title,
			String unreadcount, String update_time, String topic_type, Long create_time,
			String create_user, String clickcount, String readcount, boolean readflag,
			String content) {
		this.topic_code = topic_code;
		this.istop = istop;
		this.update_user = update_user;
		this.can_read = can_read;
		this.ucode_name = ucode_name;
		this.state = state;
		this.isself = isself;
		this.self = self;
		this.repeatcount = repeatcount;
		this.stateDesc = stateDesc;
		this.title = title;
		this.unreadcount = unreadcount;
		this.update_time = update_time;
		this.topic_type = topic_type;
		this.create_time = create_time;
		this.create_user = create_user;
		this.clickcount = clickcount;
		this.readcount = readcount;
		this.readflag = readflag;
		this.content = content;
	}
	@Generated(hash = 434965449)
	public Topic_Entity() {
	}


}
