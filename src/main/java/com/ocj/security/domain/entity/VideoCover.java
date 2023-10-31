package com.ocj.security.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName video_cover
 */
@TableName(value ="video_cover")
@Data
public class VideoCover implements Serializable {
    /**
     * 
     */
    @TableId(value = "video_id")
    private String videoId;

    /**
     * 
     */
    @TableField(value = "video_cover_url")
    private String videoCoverUrl;

    /**
     * 
     */
    @TableField(value = "width")
    private Integer width;

    /**
     * 
     */
    @TableField(value = "height")
    private Integer height;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    @TableField(value = "cover_address")
    private String coverAddress;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        VideoCover other = (VideoCover) that;
        return (this.getVideoId() == null ? other.getVideoId() == null : this.getVideoId().equals(other.getVideoId()))
            && (this.getVideoCoverUrl() == null ? other.getVideoCoverUrl() == null : this.getVideoCoverUrl().equals(other.getVideoCoverUrl()))
            && (this.getWidth() == null ? other.getWidth() == null : this.getWidth().equals(other.getWidth()))
            && (this.getHeight() == null ? other.getHeight() == null : this.getHeight().equals(other.getHeight()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getVideoId() == null) ? 0 : getVideoId().hashCode());
        result = prime * result + ((getVideoCoverUrl() == null) ? 0 : getVideoCoverUrl().hashCode());
        result = prime * result + ((getWidth() == null) ? 0 : getWidth().hashCode());
        result = prime * result + ((getHeight() == null) ? 0 : getHeight().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", videoId=").append(videoId);
        sb.append(", videoCoverUrl=").append(videoCoverUrl);
        sb.append(", width=").append(width);
        sb.append(", length=").append(height);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}